package ru.barinov.notes.ui.noteEditFragment

import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.ui.noteViewFragment.NoteViewContract

class NoteEditContract {

    interface NoteEditFragmentPresenterInterface {
        fun safeNote()

        fun checkOnEditionMode(): Boolean
    }


}
