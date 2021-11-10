package ru.barinov.notes.ui.noteListFragment

import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentResultListener
import ru.barinov.R
import ru.barinov.notes.domain.*
import ru.barinov.notes.domain.Iterator
import ru.barinov.notes.ui.AgreementDialogFragment
import ru.barinov.notes.ui.Application
import ru.barinov.notes.ui.noteEditFragment.NoteEditFragment

class NoteListFragmentPresenter: NoteListFragmentContract.NoteListFragmentPresenterInterface,  OnNoteClickListener {

    private  var view:  NoteListFragment? = null
    private lateinit var  repository: NotesRepository
    private lateinit var  adapter: NotesAdapter
    private lateinit var cache: Iterator
    private val DELETE = "OK"

    override fun onAttach(view: NoteListFragment) {
        this.view= view
        repository= (view.requireActivity().application as Application).repository
        cache = (view.requireActivity().application as Application).cache
    }

    override fun onDetach() {
        view= null
    }

    override fun onSearchStarted(search: android.widget.SearchView) {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
               cache.searchNotes(search.query.toString(), repository.allNotes)
                if (!(cache.isSearchCacheEmpty())) {
                    adapter.data=cache.getSearchedNotes()
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
            cache.clearSearchCache()
            adapter.data=repository.getNotes()
            false
        }
    }

    override fun setAdapter(adapter: NotesAdapter) {
        this.adapter= adapter
        adapter.data = repository.getNotes()
        adapter.setListener(object: OnNoteClickListener {
            override fun onClickEdit(note: NoteEntity?) {
                this@NoteListFragmentPresenter.onNoteClick(note!!)
            }

            override fun onClickDelete(note: NoteEntity) {
               this@NoteListFragmentPresenter.onClickDelete(note)
            }

            override fun onNoteClick(note: NoteEntity) {
                this@NoteListFragmentPresenter.onNoteClick(note)
            }

            override fun onNoteLongClick(note: NoteEntity, view: View) {
                this@NoteListFragmentPresenter.onNoteLongClick(note, view)

            }

            override fun onNoteChecked(note: NoteEntity) {
                this@NoteListFragmentPresenter.onNoteChecked(note)
            }

            override fun onNoteUnChecked(note: NoteEntity) {
                this@NoteListFragmentPresenter.onNoteUnChecked(note)
            }
        })
    }


    override fun getResultsFromNoteEditFragment(adapter: NotesAdapter) {
        view?.parentFragmentManager?.setFragmentResultListener(NoteEditFragment::class.simpleName!!,view!!.requireActivity(), FragmentResultListener { requestKey, result ->
            if(!(result.isEmpty)){
                val tempNote: NoteEntity = result.getParcelable(NoteEntity::class.simpleName)!!
                if (!repository.findById(tempNote.id)) {
                    Log.d("@@@", "1")
                    repository.addNote(tempNote)
                } else {
                    Log.d("@@@", "2")
                    repository.updateNote(tempNote.id, tempNote)

                }
                adapter.data= repository.getNotes()
            }

        })
    }

    override fun createNewNote(): Boolean {
        (view?.requireActivity() as Callable).callEditionFragment()
        return true
    }

    override fun deleteChosenNotes() {
        cache.getChosenNotes().forEach { repository.removeNote(it.id)
            adapter.data = repository.getNotes()}
        cache.clearSelectedCache()
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
                    adapter.data= repository.getNotes()
                }
            })
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
        if (cache.findInSelectedCache(note.id)){
            cache.removeNoteFromSelectedCache(note.id)
        }
    }
}