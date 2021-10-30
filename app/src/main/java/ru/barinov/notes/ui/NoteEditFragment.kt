package ru.barinov.notes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.Fragment
import ru.barinov.databinding.NoteEditLayoutBinding
import ru.barinov.notes.domain.NoteEntity
import java.util.*

class NoteEditFragment : Fragment() {
    lateinit var applyButton: Button
    lateinit var titleEditText: EditText
    lateinit var descriptionEditText: EditText
    lateinit var datePicker: DatePicker
    private lateinit var binding: NoteEditLayoutBinding
    var note: NoteEntity? = null
    lateinit var uuid: UUID
    var data: Bundle? = null

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


    }

    private fun initViews() {
        applyButton= binding.applyButton as Button
        titleEditText= binding.titleEdittext
        descriptionEditText= binding.descriptionEdittext
        datePicker= binding.datePickerActions
    }

    companion object {
        fun getInstance(data: Bundle?): NoteEditFragment {
            val noteEdit = NoteEditFragment()
            noteEdit.arguments = data
            return noteEdit
        }
    }
}