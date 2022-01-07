package ru.barinov.notes.ui.noteListFragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.barinov.R
import ru.barinov.notes.domain.*
import ru.barinov.notes.domain.curentDataBase.NotesCache
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity
import ru.barinov.notes.domain.noteEntityAndService.NotesAdapter
import ru.barinov.notes.domain.room.DataBase
import ru.barinov.notes.domain.telegramm.TelegrammBot
import ru.barinov.notes.ui.dataManagerFragment.DataManager
import ru.barinov.notes.ui.dialogs.AgreementDialogFragment
import java.io.IOException

class NoteListViewModel(
    private val repository: NotesRepository,
    private val adapter: NotesAdapter,
    private val cache: NotesCache,
    private val localDataBase: DataBase,
    private val authentication: Authentication,
    private val cloudDataBase: CloudRepository,
    private val activity: Callable,
    private val router: Router,
    private val sharedPref: SharedPreferences
) : OnNoteClickListener {

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

//    private val  _onNoteClicked = MutableLiveData<String>()
//    val onNoteClicked: LiveData<String> = _onNoteClicked


     fun onSearchStarted(search: android.widget.SearchView) {
        search.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(s: String): Boolean {
                cache.searchNotes(search.query.toString(), repository.getNotes())
                if (!cache.isSearchCacheEmpty()) {
                    adapter.data = cache.getSearchedNotes()
                } else {
                    _onUnsuccessfulSearch.postValue(Unit)
                }
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })
        search.setOnCloseListener {
            adapter.data = repository.getNotes()
            cache.clearSearchCache()
            false
        }
    }

    fun refreshAdapter() {
        adapter.data = repository.getNotes()
    }

     fun setAdapter() {
        refreshAdapter()
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


     fun getResultsFromNoteEditFragment(result: Bundle) {
        if (!(result.isEmpty)) {
            val note: NoteEntity = result.getParcelable(NoteEntity::class.simpleName)!!
            if (!repository.findById(note.id)) {
                Log.d("@@@", "1")
                Thread {
                    localDataBase.noteDao()
                        .addNote(note)
                }.start()
                repository.addNote(note)
                Log.d("@@@", "1-1")
                if (authentication.auth.currentUser != null && checkDataManagerSwitch()) {
                    addToCloud(note)
                }
                val textToTelegram = (note.creationDate + "\n" + note.title + "\n" + note.detail)
                Thread {
                    try {
                        telegram.getService().sendMassage(telegram.getChanelName(), textToTelegram)
                            .execute().body()
                    } catch (e: IOException) {
                        //todo
                    }
                }.start()

            } else {
                Log.d("@@@", "2")
                repository.updateNote(note.id, note)
                Thread {
                    localDataBase.noteDao().update(note)
                }.start()
                Log.d("@@@", "2-1")
                if (authentication.auth.currentUser != null && checkDataManagerSwitch()) {
                    updInCloud(note)
                }
            }

        }
        if (cache.getFavs().isNotEmpty()) {
            cache.clearFavCache()
            cache.findAllFavNotes(repository.getNotes())
            adapter.data = cache.getFavs()
        } else {
            adapter.data = repository.getNotes()
        }
    }


    private fun checkDataManagerSwitch(): Boolean {
        return sharedPref.getBoolean(
            DataManager.switchStateKey,
            false
        )
    }


    private fun updInCloud(note: NoteEntity) {
        Log.d("@@@", "Облако")
        Thread {
            cloudDataBase.cloud.collection(
                authentication.auth.currentUser?.uid.toString()
            ).document(note.id).delete()
            cloudDataBase.cloud.collection(
                authentication.auth.currentUser?.uid.toString()
            )
                .document(note.id)
        }.start()
    }


     fun createNewNote(): Boolean {
        activity.callEditionFragment()
        return true
    }

     fun deleteChosenNotes() {
        cache.getChosenNotes().forEach {
            repository.removeNote(it.id)
            Thread {
                deleteNoteInCloud(it)
            }.start()
            localDataBase.noteDao().delete(it)
            adapter.data = repository.getNotes()
        }
        cache.clearSelectedCache()
    }

     fun addToCloud(note: NoteEntity) {
        Thread {
            cloudDataBase.cloud.collection(
                authentication.auth.currentUser?.uid.toString()
            )
                .document(note.id)
                .set(note)
        }.start()
    }

    override fun onClickEdit(note: NoteEntity?) {
        _editionModeMessage.postValue(Unit)
        router.setId(note!!.id)
        activity.callEditionFragment()
    }

    override fun onClickDelete(note: NoteEntity) {
        val confirmation = AgreementDialogFragment()
        tempNote = note
        _onNoteDeletion.postValue(confirmation)
    }

    private fun deleteNoteInCloud(note: NoteEntity) {
        if (authentication.auth.currentUser != null)
            Thread {
                cloudDataBase.cloud.collection(
                    authentication.auth.currentUser?.uid.toString()
                )
                    .document(note.id).delete()
            }.start()
    }

    override fun onNoteClick(note: NoteEntity) {
//        _onNoteClicked.postValue(note.id)
        router.setId(note.id)
        activity.callNoteViewFragment()

    }

    override fun onNoteLongClick(note: NoteEntity, view: View) {
        //todo Remaster to When func
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.note_menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.delete_note_item) {
                onClickDelete(note)
            } else if (menuItem.itemId == R.id.edit_note_item) {
                onClickEdit(note)
            } else if (menuItem.itemId == R.id.reminder_item) {
                buildNotesReminder(note.id)
            }
            false
        }
        popupMenu.show()

    }

    private fun buildNotesReminder(id: String) {
        _createReminderDialog.postValue(id)
    }

    override fun onNoteChecked(note: NoteEntity) {
        cache.addSelectedNote(note)
    }

    override fun onNoteUnChecked(note: NoteEntity) {
        if (cache.findInSelectedCache(note.id)) {
            cache.removeNoteFromSelectedCache(note.id)
        }
    }

    override fun onFavButtonPressed(note: NoteEntity) {
        if (!note.isFavorite) {
            note.isFavorite = true
        } else {
            if (cache.getFavs().isNotEmpty()) {
                cache.removeFromFavs(note)
                adapter.data = cache.getFavs()
            }
            note.isFavorite = false
        }
        repository.updateNote(note.id, note)
        Thread {
            localDataBase.noteDao().update(note)
        }.start()
        Log.d("@@@", "2-1")
        if (authentication.auth.currentUser != null && checkDataManagerSwitch()) {
            updInCloud(note)
        }
    }

    fun deleteNoteInRepos(result: Bundle, key: String) {
        val isConfirmed = result.getBoolean(key)
        if (isConfirmed) {
            repository.removeNote(tempNote.id)
            Thread {
                localDataBase.noteDao()
                    .delete(tempNote)
            }.start()
            if (cache.getFavs().isNotEmpty()) {
                cache.removeFromFavs(tempNote)
                adapter.data = cache.getFavs()
            } else {
                adapter.data = repository.getNotes()
            }
            deleteNoteInCloud(tempNote)
        }
    }

    fun showOnlyFavs() {
        cache.clearFavCache()
        cache.clearSearchCache()
        cache.clearSelectedCache()
        cache.findAllFavNotes(repository.allNotes)
        adapter.data = cache.getFavs()
    }

    fun cancelShowOnlyFavs() {
        adapter.data = repository.getNotes()
        cache.clearSearchCache()
        cache.clearSelectedCache()
        cache.clearFavCache()
    }

    fun checkOnFavoritesView(): Boolean {
       return cache.getFavs().isNotEmpty()
    }
}