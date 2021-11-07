package ru.barinov.notes.ui.noteViewFragment

class NoteViewFragmentContract {

    interface NoteViewFragmentPresenterInterface{
        fun onAttach(view: NoteViewFragment)
        fun onDetach()


        fun getNote()
        fun onBackPressed()
        fun getIdFromRouter(): String?
    }
    interface ViewInterface{
        fun fillTheFields(noteTitle: String, detail: String, dateAsString: String)
    }
}