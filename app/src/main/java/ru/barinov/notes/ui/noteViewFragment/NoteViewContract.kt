package ru.barinov.notes.ui.noteViewFragment

import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity
import ru.barinov.notes.ui.noteEditFragment.NoteEditFragmentContract

class NoteViewContract {

    interface NoteViewFragmentPresenterInterface{
        var view: ViewInterface?
        var repository: NotesRepository?
        var id: String?
        fun onAttach(view: ViewInterface, repository: NotesRepository, id: String)
        fun onDetach()
        fun getNote()
    }
    interface ViewInterface{
        fun fillTheFields(noteTitle: String, detail: String, dateAsString: String)
    }
}