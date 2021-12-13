package ru.barinov.notes.ui.noteEditFragment

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
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
        val permission = ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
        presenter = NoteEditViewModel( titleEditText, descriptionEditText,
            datePicker, getIdFromRouter(),  requireActivity().application().repository, requireActivity().application().locationFinder, permission)
        presenter.startListenLocation()
        presenter.fillTheViews()
        presenter.fieldsIsNotFilledMassageLiveData.observe(requireActivity()){
            Toast.makeText(activity, R.string.warning_toast, Toast.LENGTH_SHORT).show()
        }
        presenter.viewContentLiveData.observe(requireActivity()){it->
            titleEditText.setText(it[0])
            descriptionEditText.setText(it[1])
            datePicker.updateDate(it[2].toInt(), it[3].toInt(),  it[4].toInt())
        }
        presenter.dataForFragmentResult.observe(requireActivity()){
            parentFragmentManager.setFragmentResult(
                NoteEdit::class.simpleName!!,
                it!!)
            parentFragmentManager.popBackStackImmediate()
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
        applyButton.setOnClickListener {
            presenter.initSafeNote()
        }
    }

    override fun onPause() {
        parentFragmentManager.popBackStack()
        super.onPause()
    }


    private fun getIdFromRouter(): String? {
        return (requireActivity().application as Application).router.getId()
    }

}