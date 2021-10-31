package ru.barinov.notes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.barinov.R
import ru.barinov.databinding.NoteEditLayoutBinding
import ru.barinov.notes.domain.NoteEntity
import java.util.*

class NoteEditFragment : Fragment() {
    private lateinit var applyButton: Button
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var datePicker: DatePicker
    private lateinit var binding: NoteEditLayoutBinding
    var note: NoteEntity? = null
    private lateinit var uuid: UUID
    private var data: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NoteEditLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        toFillTheNote()


    }

    private fun toFillTheNote() {
        if (checkIfEdit()) {
            note = data!!.getParcelable(NoteEntity::class.simpleName)
            titleEditText.setText(note!!.title)
            descriptionEditText.setText(note!!.detail)
            datePicker.updateDate(note!!.originYear, note!!.originMonth - 1, note!!.originDay)
        }
    }

    private fun initViews() {
        initButton()
        titleEditText = binding.titleEdittext
        descriptionEditText = binding.descriptionEdittext
        datePicker = binding.datePickerActions
    }

    private fun initButton() {
        applyButton = binding.applyButton as Button
        applyButton.setOnClickListener {
            uuid = UUID.randomUUID()
            //Редактирование
            if (checkIfEdit() && (titleEditText.text.isNotEmpty() && descriptionEditText.text.isNotEmpty())) {
                note = NoteEntity(
                    note!!.id, titleEditText.text.toString(),
                    descriptionEditText.text.toString(),
                    datePicker.dayOfMonth, datePicker.month, datePicker.year)
                data = Bundle()
                data!!.putParcelable(NoteEntity::class.simpleName, note)
                parentFragmentManager.setFragmentResult(
                    NoteEditFragment::class.simpleName!!,
                    data!!)
                parentFragmentManager.popBackStackImmediate()
            }//Создаём новую заметку
            else if(titleEditText.text.isNotEmpty() && descriptionEditText.text.isNotEmpty()){
                note = NoteEntity(
                    uuid.toString(), titleEditText.text.toString(),
                    descriptionEditText.text.toString(),
                    datePicker.dayOfMonth, datePicker.month, datePicker.year)
                data = Bundle()
                data!!.putParcelable(NoteEntity::class.simpleName, note)
                parentFragmentManager.setFragmentResult(
                    NoteEditFragment::class.simpleName!!,
                    data!!)
                parentFragmentManager.popBackStackImmediate()
            } else {
            Toast.makeText(activity, R.string.warning_toast, Toast.LENGTH_SHORT).show()
        }
        }
    }

    private fun checkIfEdit(): Boolean {
        if (!(arguments ==null)) {
            if (data == null) {
                data = arguments
            }
            return true
        }
        return false
    }

    companion object {
        fun getInstance(data: Bundle?): NoteEditFragment {
            val noteEdit = NoteEditFragment()
            noteEdit.arguments = data
            return noteEdit
        }
    }
}