package ru.barinov.notes.ui.noteViewFragment

import ru.barinov.notes.domain.curentDataBase.NotesRepository

class NoteViewContract {

    interface NoteViewFragmentPresenterInterface{
        fun getNote()
    }
    interface ViewInterface{
        fun fillTheFields(noteTitle: String, detail: String, dateAsString: String)
    }
}