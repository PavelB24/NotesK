package ru.barinov.notes.ui.noteEditFragment

import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity
import java.util.*


class NoteEditViewModel(
    private val applyButton: Button,
    private val title: EditText,
    private val body: EditText,
    private val datePicker: DatePicker,
    id: String?,
    private val manager: FragmentManager,
    repository: NotesRepository
) : NoteEditContract.NoteEditFragmentPresenterInterface {
    private var tempNote: NoteEntity? = repository.getById(id)
    private lateinit var uuid: UUID
    private var data: Bundle? = null

    private val _fieldsIsNotFilledMassageLiveData= MutableLiveData<Boolean>()
    val fieldsIsNotFilledMassageLiveData: LiveData<Boolean> = _fieldsIsNotFilledMassageLiveData

    private val _viewContentLiveData= MutableLiveData<Array<String>>()
    val viewContentLiveData: LiveData<Array<String>> = _viewContentLiveData


    //Переписать под получение строк и интов, а не вьюшек
    override fun safeNote() {
        applyButton.setOnClickListener {
            uuid = UUID.randomUUID()
            //Редактирование
            if (checkOnEditionMode() && (title.text.toString().isNotEmpty() && body.text.toString().isNotEmpty())) {
                val note = NoteEntity(
                    tempNote!!.id, title.text.toString(),
                    body.text.toString(),
                    datePicker.dayOfMonth, datePicker.month, datePicker.year
                )
                data = Bundle()
                data?.putParcelable(NoteEntity::class.simpleName, note)
                manager.setFragmentResult(
                    NoteEdit::class.simpleName!!,
                    data!!
                )
                manager.popBackStackImmediate()
            }//Создаём новую заметку
            else if (title.text.toString().isNotEmpty() && body.text.toString().isNotEmpty()) {
                val note = NoteEntity(
                    uuid.toString(), title.text.toString(),
                    body.text.toString(),
                    datePicker.dayOfMonth, datePicker.month, datePicker.year
                )
                data = Bundle()
                data?.putParcelable(NoteEntity::class.simpleName, note)
                manager.setFragmentResult(
                    NoteEdit::class.simpleName!!,
                    data!!
                )
                manager.popBackStackImmediate()
            } else {
                _fieldsIsNotFilledMassageLiveData.postValue(true)
            }
        }
    }

    override fun checkOnEditionMode(): Boolean {
        if (!(tempNote == null)) {
            return true
        }
        return false
    }



    fun fillTheViews() {
        if (checkOnEditionMode()) {
         _viewContentLiveData.postValue(
             arrayOf(tempNote!!.title,
                 tempNote!!.detail,
                 tempNote!!.originYear.toString(),
                 tempNote!!.originMonth.toString(),
                 tempNote!!.originDay.toString())
            )
        }
    }


}


