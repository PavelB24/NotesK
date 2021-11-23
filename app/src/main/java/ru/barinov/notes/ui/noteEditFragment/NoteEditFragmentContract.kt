package ru.barinov.notes.ui.noteEditFragment

import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.ui.noteViewFragment.NoteViewContract

class NoteEditFragmentContract {

    interface NoteEditFragmentPresenterInterface {
        fun onAttach(view: NoteEditFragment)
        fun onDetach()

        fun safeNote(
            applyButton: Button,
            titleEditText: EditText,
            descriptionEditText: EditText,
            datePicker: DatePicker
        )

        fun checkOnEditionMode(): Boolean
    }

    interface ViewInterface {
        fun fillTheFields(title: String?, detail: String?, year: Int?, month: Int?, day: Int?)
        fun fieldsIsNotFilledMassageToast()
    }
}
