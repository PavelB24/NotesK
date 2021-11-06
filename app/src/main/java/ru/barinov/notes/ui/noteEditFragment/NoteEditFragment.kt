package ru.barinov.notes.ui.noteEditFragment

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
import ru.barinov.notes.ui.notesActivity.NoteActivityPresenter
import java.util.*

class NoteEditFragment() : Fragment(), NoteEditFragmentContract.ViewInterface {
    private lateinit var applyButton: Button
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var datePicker: DatePicker
    private lateinit var binding: NoteEditLayoutBinding
    private var presenter = NoteEditFragmentPresenter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NoteEditLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.onAttach(this)
        initViews()
        initButton()
        presenter.fillTheViews()
        super.onViewCreated(view, savedInstanceState)
    }



    private fun initViews() {
        titleEditText = binding.titleEdittext
        descriptionEditText = binding.descriptionEdittext
        datePicker = binding.datePickerActions
    }

    private fun initButton() {
        applyButton = binding.applyButton as Button
        presenter.safeNote(applyButton, titleEditText, descriptionEditText, datePicker)

    }


    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    override fun fillTheFields(title: String?, detail: String?, year: Int?, month: Int?, day: Int?) {
            titleEditText.setText(title)
            descriptionEditText.setText(detail)
            datePicker.updateDate(year!!, month!! - 1, day!!)
    }

    override fun fieldsIsNotFilledMassageToast() {
        Toast.makeText(activity, R.string.warning_toast, Toast.LENGTH_SHORT).show()
    }

}