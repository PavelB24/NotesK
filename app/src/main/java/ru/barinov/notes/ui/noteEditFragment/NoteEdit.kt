package ru.barinov.notes.ui.noteEditFragment

import android.content.Context
import android.location.LocationManager
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
import ru.barinov.notes.ui.Application
import ru.barinov.notes.ui.application

class NoteEdit() : Fragment() {
    private lateinit var applyButton: Button
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var datePicker: DatePicker
    private lateinit var binding: NoteEditLayoutBinding
    private lateinit var presenter: NoteEditViewModel
    //todo
    private val location= (requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NoteEditLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        initButton()
        presenter = NoteEditViewModel(applyButton, titleEditText, descriptionEditText,
            datePicker, getIdFromRouter(), parentFragmentManager, requireActivity().application().repository )
        presenter.safeNote()
        presenter.fillTheViews()
        presenter.fieldsIsNotFilledMassageLiveData.observe(requireActivity()){
            Toast.makeText(activity, R.string.warning_toast, Toast.LENGTH_SHORT).show()
        }
        presenter.viewContentLiveData.observe(requireActivity()){it->
            titleEditText.setText(it[0])
            descriptionEditText.setText(it[1])
            datePicker.updateDate(it[2].toInt(), it[3].toInt(),  it[4].toInt())

        }
        super.onViewCreated(view, savedInstanceState)
    }


    private fun initViews() {
        titleEditText = binding.titleEdittext
        descriptionEditText = binding.descriptionEdittext
        datePicker = binding.datePickerActions
    }

    private fun initButton() {
        applyButton = binding.applyButton as Button
    }

    override fun onPause() {
        parentFragmentManager.popBackStack()
        super.onPause()
    }


    private fun getIdFromRouter(): String? {
        return (requireActivity().application as Application).router.getId()
    }

}