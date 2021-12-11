package ru.barinov.notes.ui.noteListFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.barinov.R
import ru.barinov.notes.domain.*
import ru.barinov.notes.domain.curentDataBase.Iterator
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity
import ru.barinov.notes.domain.noteEntityAndService.NotesAdapter
import ru.barinov.notes.domain.room.DataBase
import ru.barinov.notes.domain.telegramm.TelegrammBot
import ru.barinov.notes.ui.AgreementDialogFragment

class NoteListViewModel(
    private val repository: NotesRepository,
    private val adapter: NotesAdapter,
    private val cache: Iterator,
    private val localDataBase: DataBase,
    private val authentication: Authentication,
    private val cloudDataBase: CloudRepository,
    private val activity: Callable,
    private val router: Router
) :
    NoteListContract.NoteListFragmentPresenterInterface, OnNoteClickListener {

    private val telegram = TelegrammBot()
    private lateinit var tempNote: NoteEntity

    private val _onNoteDeletion = MutableLiveData<DialogFragment>()
    val onNoteDeletion: LiveData<DialogFragment> = _onNoteDeletion

    private val  _editionModeMessage = MutableLiveData<Boolean>()
    val editionModeMessage: LiveData<Boolean> = _editionModeMessage

    private val  _onUnsuccessfulSearch = MutableLiveData<Boolean>()
    val onUnsuccessfulSearch: LiveData<Boolean> = _onUnsuccessfulSearch


    override fun onSearchStarted(search: android.widget.SearchView) {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                cache.searchNotes(search.query.toString(), repository.allNotes)
                if (!(cache.isSearchCacheEmpty())) {
                    adapter.data = cache.getSearchedNotes()
                } else {
                    _onUnsuccessfulSearch.postValue(true)
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

    fun refreshAdapter(){
        adapter.data= repository.getNotes()
    }

    override fun setAdapter() {
        adapter.data = repository.getNotes()
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
        })
    }


    override fun getResultsFromNoteEditFragment(result: Bundle, switchState: Boolean) {
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
                if (authentication.auth.currentUser != null && switchState) {
                    addToCloud(note)
                }
                val textToTelegram = (note.dateAsString + "\n" + note.title + "\n" + note.detail)
                Thread {
                    telegram.getService().sendMassage(telegram.getChanelName(), textToTelegram)
                        .execute().body()
                }.start()

            } else {
                Log.d("@@@", "2")
                repository.updateNote(note.id, note)
                Thread {
                    localDataBase.noteDao().update(note)
                }.start()
                Log.d("@@@", "2-1")
                if (authentication.auth.currentUser != null && switchState) {
                    updInCloud(note)
                }
            }

        }
        adapter.data = repository.getNotes()
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
                .set(note).addOnSuccessListener { Log.d("@@@", "Added to cloud") }
                .addOnFailureListener { Log.d("@@@", "Not added to cloud") }
        }.start()
    }


    override fun createNewNote(): Boolean {
        activity.callEditionFragment()
        return true
    }

    override fun deleteChosenNotes() {
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

    override fun addToCloud(note: NoteEntity) {
        Thread {
            cloudDataBase.cloud.collection(
                authentication.auth.currentUser?.uid.toString()
            )
                .document(note.id)
                .set(note).addOnSuccessListener { Log.d("@@@", "Added to cloud") }
                .addOnFailureListener { Log.d("@@@", "Not added to cloud") }
        }.start()
    }

    override fun onClickEdit(note: NoteEntity?) {
        //todo liveData
        _editionModeMessage.postValue(true)
        router.setId(note!!.id)
        activity.callEditionFragment()
    }

    override fun onClickDelete(note: NoteEntity) {
        val confirmation = AgreementDialogFragment()
        //todo LD with DialogFragment
        tempNote = note
        _onNoteDeletion.postValue(confirmation)
    }

    private fun deleteNoteInCloud(note: NoteEntity) {
        if (authentication.auth.currentUser != null)
            Thread {
                cloudDataBase.cloud.collection(
                        authentication.auth.currentUser?.uid.toString())
                    .document(note.id).delete()
            }.start()
    }

    override fun onNoteClick(note: NoteEntity) {
        router.setId(note.id)
        activity.callNoteViewFragment()

    }

    override fun onNoteLongClick(note: NoteEntity, view: View) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.note_menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.delete_note_item) {
                onClickDelete(note)
            } else if (menuItem.itemId == R.id.edit_note_item) {
                onClickEdit(note)
            }
            false
        }
        popupMenu.show()

    }

    override fun onNoteChecked(note: NoteEntity) {
        cache.addSelectedNote(note)
    }

    override fun onNoteUnChecked(note: NoteEntity) {
        if (cache.findInSelectedCache(note.id)) {
            cache.removeNoteFromSelectedCache(note.id)
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
            adapter.data = repository.getNotes()
            deleteNoteInCloud(tempNote)
        }
    }
}