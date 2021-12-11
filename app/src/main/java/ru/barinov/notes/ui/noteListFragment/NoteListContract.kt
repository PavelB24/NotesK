package ru.barinov.notes.ui.noteListFragment

import android.os.Bundle
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity
import ru.barinov.notes.domain.noteEntityAndService.NotesAdapter

class NoteListContract {

    interface NoteListFragmentPresenterInterface{
        fun onSearchStarted(search: android.widget.SearchView)
        fun setAdapter()
        fun getResultsFromNoteEditFragment(result: Bundle, switchState: Boolean)
        fun createNewNote() : Boolean
        fun deleteChosenNotes()
        fun addToCloud(note: NoteEntity)

    }

}