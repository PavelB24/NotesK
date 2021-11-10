package ru.barinov.notes.ui.notesActivity

import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView


class NotesActivityContract {


    interface NoteActivityPresenterInterface{
        fun onAttach(view: NotesActivity)
        fun onDetach()

        fun onSafeNotes()
        fun onReadNotes()
        fun onChoseNavigationItem()
        fun toInitNotesInRepository()
        fun setNavigationListeners(bottomNavigationItemView: BottomNavigationView)
        fun editNote( )
        fun openNote()


    }
}