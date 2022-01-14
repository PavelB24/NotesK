package ru.barinov.notes.ui.noteListFragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.barinov.R
import ru.barinov.notes.core.liveData.combine
import ru.barinov.notes.domain.*
import ru.barinov.notes.domain.userRepository.NotesRepository
import ru.barinov.notes.domain.interfaces.ActivityCallableInterface
import ru.barinov.notes.domain.interfaces.OnNoteClickListener
import ru.barinov.notes.domain.models.NoteEntity
import ru.barinov.notes.domain.adapters.NotesAdapter
import ru.barinov.notes.domain.telegramm.TelegrammBot
import ru.barinov.notes.ui.dataManagerFragment.DataManagerFragment
import ru.barinov.notes.ui.dialogs.AgreementDialogFragment

class NoteListViewModel(
    private val notesRepository: NotesRepository,
    private val adapter: NotesAdapter,
    private val cloudDataBase: CloudRepository,
    private val activity: ActivityCallableInterface,
    private val sharedPreferences: SharedPreferences
) : OnNoteClickListener, ViewModel() {

    private val telegram = TelegrammBot()
    private lateinit var tempNote: NoteEntity

    private val _onNoteDeletion = MutableLiveData<DialogFragment>()
    val onNoteDeletion: LiveData<DialogFragment> = _onNoteDeletion

    private val _editionModeMessage = MutableLiveData<Unit>()
    val editionModeMessage: LiveData<Unit> = _editionModeMessage

    private val _onUnsuccessfulSearch = MutableLiveData<Unit>()
    val onUnsuccessfulSearch: LiveData<Unit> = _onUnsuccessfulSearch

    private val _createReminderDialog = MutableLiveData<String>()
    val createReminderDialog: LiveData<String> = _createReminderDialog

    private val _isFavoriteSelected = MutableLiveData(false)
    private val isFavoriteSelected: LiveData<Boolean> = _isFavoriteSelected

    private val _searchedNotes = MutableLiveData<String>("")
    val searchedNotes: LiveData<String> = _searchedNotes

    private val _checkedNotes = MutableLiveData<MutableList<NoteEntity>>(mutableListOf())
    private val checkedNotes: LiveData<MutableList<NoteEntity>> = _checkedNotes

    val noteListLiveData: LiveData<List<NoteEntity>> = combine(
        notesRepository.getNotesLiveData(), isFavoriteSelected, searchedNotes
    ) { allNotes, favorite, query ->
        return@combine if (favorite) {
            if (query.isNotEmpty()) {
                allNotes.filter { noteEntity -> noteEntity.isFavorite }
                    .filter { noteEntity -> noteEntity.title.contains(query, ignoreCase = true) }
            } else {
                allNotes.filter { noteEntity -> noteEntity.isFavorite }
            }
        } else {
            if (query.isNotEmpty()) {
                allNotes.filter { noteEntity -> noteEntity.title.contains(query, ignoreCase = true) }
            } else {
                allNotes
            }
        }
    }

    fun onSearchStarted(search: android.widget.SearchView) {
        search.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                _searchedNotes.value = query
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                _searchedNotes.value = query
                return false
            }
        })
        search.setOnCloseListener {
            _searchedNotes.value = ""
            false
        }
    }

    fun onFavoriteClicked(selected: Boolean) {
        _isFavoriteSelected.value = selected
    }

    fun setAdapter() {
        adapter.setListener(object : OnNoteClickListener {
            override fun onClickEdit(note: NoteEntity?) {
                this@NoteListViewModel.onNoteClick(note!!)
            }

            override fun onClickDelete(note: NoteEntity) {
                this@NoteListViewModel.onClickDelete(note)
            }

            override fun onNoteClick(note: NoteEntity) {
                this@NoteListViewModel.onNoteClick(note)
            }

            override fun onNoteLongClick(note: NoteEntity, view: View) {
                this@NoteListViewModel.onNoteLongClick(note, view)

            }

            override fun onNoteChecked(note: NoteEntity) {
                this@NoteListViewModel.onNoteChecked(note)
            }

            override fun onNoteUnChecked(note: NoteEntity) {
                this@NoteListViewModel.onNoteUnChecked(note)
            }

            override fun onFavButtonPressed(note: NoteEntity) {
                this@NoteListViewModel.onFavButtonPressed(note)
            }
        })
    }

    private fun checkDataManagerSwitch(): Boolean {
        return sharedPreferences.getBoolean(
            DataManagerFragment.switchStateKey, false
        )
    }

    fun deleteChosenNotes() {
        checkedNotes.observeForever { checkedLst ->
            checkedLst.forEach { note ->
                notesRepository.removeNote(note)
                Thread {
                    if (checkDataManagerSwitch()){
                    cloudDataBase.deleteNoteInCloud(note)}
                }.start()
            }
        }
    }

    override fun onClickEdit(note: NoteEntity?) {
        //todo передать заметку в аргументсы
        _editionModeMessage.postValue(Unit)
        activity.callEditionFragment(note!!.id)
    }

    override fun onClickDelete(note: NoteEntity) {
        val confirmation = AgreementDialogFragment()
        tempNote = note
        _onNoteDeletion.postValue(confirmation)
    }

    override fun onNoteClick(note: NoteEntity) {
        activity.callNoteViewFragment(note.id)

    }

    override fun onNoteLongClick(note: NoteEntity, view: View) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.note_menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.delete_note_item -> {
                    onClickDelete(note)
                }
                R.id.edit_note_item -> {
                    onClickEdit(note)
                }
                R.id.reminder_item -> {
                    buildNotesReminder(note.id)
                }
            }
            false
        }
        popupMenu.show()

    }

    private fun buildNotesReminder(id: String) {
        _createReminderDialog.postValue(id)
    }

    override fun onNoteChecked(note: NoteEntity) {
        _checkedNotes.value!!.add(note)
    }

    override fun onNoteUnChecked(note: NoteEntity) {
        _checkedNotes.value!!.remove(note)
    }

    override fun onFavButtonPressed(note: NoteEntity) {
        note.isFavorite = !note.isFavorite
        notesRepository.insertNote(note)
        Log.d("@@@", "2-1")
        if (checkDataManagerSwitch()) {
            Thread {
                cloudDataBase.writeInCloud(note)
            }.start()
        }
    }

    fun deleteNoteInRepos(result: Bundle, key: String) {
        val isConfirmed = result.getBoolean(key)
        if (isConfirmed) {
            notesRepository.removeNote(tempNote)
        }
        Thread {
            cloudDataBase.deleteNoteInCloud(tempNote)
        }.start()
    }
}

