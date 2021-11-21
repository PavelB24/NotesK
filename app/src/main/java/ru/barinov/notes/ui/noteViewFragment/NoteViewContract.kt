package ru.barinov.notes.ui.noteViewFragment

class NoteViewContract {

    interface NoteViewFragmentPresenterInterface{
        fun onAttach(view: NoteView)
        fun onDetach()


        fun getNote()
        fun onBackPressed()
        fun getIdFromRouter(): String?
    }
    interface ViewInterface{
        fun fillTheFields(noteTitle: String, detail: String, dateAsString: String)
    }
}