package ru.barinov.notes.ui.noteEditFragment

import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import ru.barinov.notes.domain.NoteEntity
import ru.barinov.notes.ui.Application
import java.util.*


class NoteEditFragmentPresenter: NoteEditFragmentContract.NoteEditFragmentPresenterInterface {
    private var view:  NoteEditFragment? = null
    private  var  note: NoteEntity? = null
    private lateinit var uuid: UUID
    private  var data: Bundle? = null

    override fun onAttach(view: NoteEditFragment) {
        this.view = view
        note = (view.requireActivity().application as Application).repository.noteCache
        (view.requireActivity().application as Application).repository.noteCache = null
    }

    override fun onDetach() {
        data = null
        view= null
    }



    override fun safeNote(applyButton: Button, titleEditText: EditText, descriptionEditText: EditText, datePicker: DatePicker) {
        applyButton.setOnClickListener {
            uuid = UUID.randomUUID()
            //Редактирование
            if (checkOnEditionMode() && (titleEditText.text.isNotEmpty() && descriptionEditText.text.isNotEmpty())) {
                note = NoteEntity(
                    note!!.id, titleEditText.text.toString(),
                    descriptionEditText.text.toString(),
                    datePicker.dayOfMonth, datePicker.month, datePicker.year
                )
                data = Bundle()
                data?.putParcelable(NoteEntity::class.simpleName, note)
                view?.parentFragmentManager?.setFragmentResult(
                    NoteEditFragment::class.simpleName!!,
                    data!!
                )
                view?.parentFragmentManager?.popBackStackImmediate()
            }//Создаём новую заметку
            else if (titleEditText.text.isNotEmpty() && descriptionEditText.text.isNotEmpty()) {
                note = NoteEntity(
                    uuid.toString(), titleEditText.text.toString(),
                    descriptionEditText.text.toString(),
                    datePicker.dayOfMonth, datePicker.month, datePicker.year
                )
                data = Bundle()
                data?.putParcelable(NoteEntity::class.simpleName, note)
                view?.parentFragmentManager?.setFragmentResult(
                    NoteEditFragment::class.simpleName!!,
                    data!!)
                view?.parentFragmentManager?.popBackStackImmediate()
            } else {
               view?.fieldsIsNotFilledMassageToast()
            }
        }
    }

   override fun checkOnEditionMode(): Boolean {
        if (!(note == null)) {
            view?.fillTheFields(note?.title, note?.detail, note?.originYear, note?.originMonth, note?.originDay)
            return true
        }
        return false
    }


}


