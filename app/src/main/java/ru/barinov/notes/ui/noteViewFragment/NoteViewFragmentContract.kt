package ru.barinov.notes.ui.noteViewFragment

class NoteViewFragmentContract {

    interface NoteViewFragmentPresenterInterface{
        fun getNote()
    }
    interface NoteViewFragmentInterface{
        fun fillTheFields()
    }
}