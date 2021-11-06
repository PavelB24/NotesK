package ru.barinov.notes.ui.notesActivity

import android.view.View


class NotesActivityContract {


    interface NoteActivityPresenterInterface{
        fun onAttach(view: NotesActivity)
        fun onDetach()

        fun onSafeNotes()
        fun onReadNotes()
        fun onChoseNavigationItem()
        fun toInitNotesInRepository()

    }
}