package ru.barinov.notes.ui.noteEditFragment

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import ru.barinov.notes.domain.NoteEntity
import ru.barinov.notes.ui.Application
import java.util.*


class NoteEditFragmentPresenter : NoteEditFragmentContract.NoteEditFragmentPresenterInterface {
    private var view: NoteEditFragment? = null
    private var tempNote: NoteEntity? = null
    private lateinit var uuid: UUID
    private var data: Bundle? = null

    override fun onAttach(view: NoteEditFragment) {
        this.view = view
        tempNote = (view.requireActivity().application as Application).repository.getById(getIdFromRouter())
        (view.requireActivity().application as Application).router.resetId()
    }

    override fun onDetach() {
        data = null
        view = null
        tempNote = null
    }

//Переписать под получение строк и интов, а не вьюшек
    override fun safeNote(
        applyButton: Button,
        titleEditText: EditText,
        descriptionEditText: EditText,
        datePicker: DatePicker
    ) {
        applyButton.setOnClickListener {
            uuid = UUID.randomUUID()
            //Редактирование
            if (checkOnEditionMode() && (titleEditText.text.isNotEmpty() && descriptionEditText.text.isNotEmpty())) {
                val note = NoteEntity(
                    tempNote!!.id, titleEditText.text.toString(),
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
                val note = NoteEntity(
                    uuid.toString(), titleEditText.text.toString(),
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
            } else {
                view?.fieldsIsNotFilledMassageToast()
            }
        }
    }

    override fun checkOnEditionMode(): Boolean {
        if (!(tempNote == null)) {
            return true
        }
        return false
    }

    private fun getIdFromRouter(): String? {
        return (view?.requireActivity()?.application as Application).router.getId()
    }

    fun fillTheViews() {
        if (checkOnEditionMode()) {
            view?.fillTheFields(
                tempNote?.title,
                tempNote?.detail,
                tempNote?.originYear,
                tempNote?.originMonth,
                tempNote?.originDay
            )
        }
    }


}


