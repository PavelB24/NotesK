package ru.barinov.notes.ui.noteListFragment

import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentResultListener
import ru.barinov.R
import ru.barinov.notes.domain.*
import ru.barinov.notes.domain.curentDataBase.Iterator
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity
import ru.barinov.notes.domain.noteEntityAndService.NotesAdapter
import ru.barinov.notes.ui.AgreementDialogFragment
import ru.barinov.notes.ui.Application
import ru.barinov.notes.ui.application
import ru.barinov.notes.ui.dataManagerFragment.DataManager
import ru.barinov.notes.ui.dataManagerFragment.DataManagerPresenter
import ru.barinov.notes.ui.noteEditFragment.NoteEditFragment

class NoteListPresenter : NoteListContract.NoteListFragmentPresenterInterface, OnNoteClickListener {

    private var view: NoteList? = null
    private lateinit var repository: NotesRepository
    private lateinit var adapter: NotesAdapter
    private lateinit var cache: Iterator
    private val DELETE = "OK"

    override fun onAttach(view: NoteList) {
        this.view = view
        repository = (view.requireActivity().application as Application).repository
        cache = (view.requireActivity().application as Application).cache
    }

    override fun onDetach() {
        view = null
        cache.clearSelectedCache()
        cache.clearSearchCache()
    }

    override fun onSearchStarted(search: android.widget.SearchView) {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                cache.searchNotes(search.query.toString(), repository.allNotes)
                if (!(cache.isSearchCacheEmpty())) {
                    adapter.data = cache.getSearchedNotes()
                } else {
                    view?.searchWasUnsuccessfulMessage()
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

    override fun setAdapter(adapter: NotesAdapter) {
        this.adapter = adapter
        adapter.data = repository.getNotes()
        adapter.setListener(object : OnNoteClickListener {
            override fun onClickEdit(note: NoteEntity?) {
                this@NoteListPresenter.onNoteClick(note!!)
            }

            override fun onClickDelete(note: NoteEntity) {
                this@NoteListPresenter.onClickDelete(note)
            }

            override fun onNoteClick(note: NoteEntity) {
                this@NoteListPresenter.onNoteClick(note)
            }

            override fun onNoteLongClick(note: NoteEntity, view: View) {
                this@NoteListPresenter.onNoteLongClick(note, view)

            }

            override fun onNoteChecked(note: NoteEntity) {
                this@NoteListPresenter.onNoteChecked(note)
            }

            override fun onNoteUnChecked(note: NoteEntity) {
                this@NoteListPresenter.onNoteUnChecked(note)
            }
        })
    }


    override fun getResultsFromNoteEditFragment(adapter: NotesAdapter) {
        view?.parentFragmentManager?.setFragmentResultListener(
            NoteEditFragment::class.simpleName!!,
            view!!.requireActivity(),
            FragmentResultListener { requestKey, result ->
                Log.d("@@@", "Зашёл сюда")
                if (!(result.isEmpty)) {
                    val note: NoteEntity = result.getParcelable(NoteEntity::class.simpleName)!!
                    if (!repository.findById(note.id)) {
                        Log.d("@@@", "1")
                        Thread {
                            (view!!.requireActivity().application()).localDataBase.noteDao()
                                .addNote(note)
                        }.start()
                        repository.addNote(note)
                        Log.d("@@@", "1-1")
                        if (view!!.requireActivity()
                                .application().authentication.auth.currentUser != null){
                        addToCloud(note)}
                    } else {
                        Log.d("@@@", "2")
                        repository.updateNote(note.id, note)
                        Thread {
                            (view!!.requireActivity().application()).localDataBase.noteDao()
                                .update(note)
                        }.start()
                        Log.d("@@@", "2-1")
                        if (view!!.requireActivity()
                                .application().authentication.auth.currentUser != null) {
                            updInCloud(note)
                        }
                    }

                }
                adapter.data = repository.getNotes()
            })
    }

    private fun updInCloud(note: NoteEntity) {
        Log.d("@@@", "Облако")
        Thread {
            view!!.requireActivity().application().cloudDataBase.cloud.collection(
                view!!.requireActivity()
                    .application().authentication.auth.currentUser?.uid.toString()
            ).document(note.id).delete()
            view!!.requireActivity().application().cloudDataBase.cloud.collection(
                view!!.requireActivity()
                    .application().authentication.auth.currentUser?.uid.toString()
            )
                .document(note.id)
                .set(note).addOnSuccessListener { Log.d("@@@", "Added to cloud") }
                .addOnFailureListener { Log.d("@@@", "Not added to cloud") }
        }.start()
    }


    override fun createNewNote(): Boolean {
        (view?.requireActivity() as Callable).callEditionFragment()
        return true
    }

    override fun deleteChosenNotes() {
        cache.getChosenNotes().forEach {
            repository.removeNote(it.id)
            Thread {
                deleteNoteInCloud(it)
            }.start()
            view!!.requireActivity().application().localDataBase.noteDao().delete(it)
            adapter.data = repository.getNotes()
        }
        cache.clearSelectedCache()
    }

    override fun addToCloud(note: NoteEntity) {
        Thread {
            (view!!.requireActivity().application().cloudDataBase.cloud.collection(
                view!!.requireActivity()
                    .application().authentication.auth.currentUser?.uid.toString()
            )
                .document(note.id)
                .set(note).addOnSuccessListener { Log.d("@@@", "Added to cloud") }
                .addOnFailureListener { Log.d("@@@", "Not added to cloud") })
        }.start()
    }

    override fun onClickEdit(note: NoteEntity?) {
        view?.onEditionModeToastMessage()
        (view?.requireActivity()?.application as Application).router.setId(note!!.id)
        (view?.requireActivity() as Callable).callEditionFragment()
    }

    override fun onClickDelete(note: NoteEntity) {
        val confirmation = AgreementDialogFragment()
        confirmation.show(view!!.parentFragmentManager, DELETE)
        view?.parentFragmentManager?.setFragmentResultListener(
            AgreementDialogFragment::class.simpleName!!,
            view!!.requireActivity(), { requestKey, result ->
                val isConfirmed = result.getBoolean(confirmation.AGREEMENT_KEY)
                if (isConfirmed) {
                    repository.removeNote(note.id)
                    Thread {
                        (view!!.requireActivity().application()).localDataBase.noteDao()
                            .delete(note)
                    }.start()
                    adapter.data = repository.getNotes()
                    deleteNoteInCloud(note)
                }
            })
    }

    private fun deleteNoteInCloud(note: NoteEntity) {
        if (view!!.requireActivity().application().authentication.auth.currentUser != null)
            Thread {
                view!!.requireActivity().application().cloudDataBase.cloud
                    .collection(
                        view!!.requireActivity()
                            .application().authentication.auth.currentUser?.uid.toString()
                    )
                    .document(note.id).delete()
            }.start()
    }

    override fun onNoteClick(note: NoteEntity) {
        (view?.requireActivity()?.application as Application).router.setId(note.id)
        (view?.requireActivity() as Callable).callNoteViewFragment()

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
}