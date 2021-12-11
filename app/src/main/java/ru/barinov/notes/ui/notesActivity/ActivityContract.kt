package ru.barinov.notes.ui.notesActivity

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView


class ActivityContract {


    interface NoteActivityPresenterInterface{
        fun safeNotes()
        fun readNotes()
        fun onChoseNavigationItem()
        fun toInitNotesInRepository()


    }
}