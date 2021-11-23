package ru.barinov.notes.ui.noteListFragment

import ru.barinov.notes.domain.noteEntityAndService.NoteEntity
import ru.barinov.notes.domain.noteEntityAndService.NotesAdapter

class NoteListContract {

    interface NoteListFragmentPresenterInterface{
        fun onAttach(view: NoteList)
        fun onDetach()

        fun onSearchStarted(search: android.widget.SearchView)
        fun setAdapter(adapter: NotesAdapter)
        fun getResultsFromNoteEditFragment(adapter: NotesAdapter)
        fun createNewNote() : Boolean
        fun deleteChosenNotes()
        fun addToCloud(note: NoteEntity)

    }
    interface View{
        fun searchWasUnsuccessfulMessage()
        fun onEditionModeToastMessage()

    }
}