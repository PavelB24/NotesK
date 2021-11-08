package ru.barinov.notes.ui.noteListFragment

import ru.barinov.notes.domain.NotesAdapter

class NoteListFragmentContract {

    interface NoteListFragmentPresenterInterface{
        fun onAttach(view: NoteListFragment)
        fun onDetach()

        fun onSearchStarted(search: android.widget.SearchView)
        fun setAdapter(adapter: NotesAdapter)
        fun getResultsFromNoteEditFragment(adapter: NotesAdapter)
        fun createNewNote() : Boolean
        fun deleteChosenNotes()

    }
    interface View{
        fun searchWasUnsuccessfulMessage()
        fun onEditionModeToastMessage()

    }
}