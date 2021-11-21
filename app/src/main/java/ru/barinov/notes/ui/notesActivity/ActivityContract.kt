package ru.barinov.notes.ui.notesActivity

import com.google.android.material.bottomnavigation.BottomNavigationView


class ActivityContract {


    interface NoteActivityPresenterInterface{
        fun onAttach(view: Activity)
        fun onDetach()

        fun safeNotes()
        fun readNotes()
        fun onChoseNavigationItem()
        fun toInitNotesInRepository()
        fun setNavigationListeners(bottomNavigationItemView: BottomNavigationView)
        fun editNote()
        fun openNote()


    }
}