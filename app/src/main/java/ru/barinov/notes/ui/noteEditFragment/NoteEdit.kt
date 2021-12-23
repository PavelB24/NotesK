package ru.barinov.notes.ui.noteEditFragment

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import ru.barinov.R
import ru.barinov.databinding.NoteEditLayoutBinding
import ru.barinov.notes.ui.Application
import ru.barinov.notes.ui.application
import ru.barinov.notes.ui.notesActivity.Activity

class NoteEdit() : Fragment() {
    private lateinit var applyButton: Button
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
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
        val permission = ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
        presenter = NoteEditViewModel( titleEditText, descriptionEditText,
            getIdFromRouter(),  requireActivity().application().repository, requireActivity().application().locationFinder, permission)
        presenter.startListenLocation()
        presenter.fillTheViews()

        initButton()
        presenter.fieldsIsNotFilledMassageLiveData.observe(requireActivity()){
            Toast.makeText(activity, R.string.warning_toast, Toast.LENGTH_SHORT).show()
        }
        presenter.viewContentLiveData.observe(requireActivity()){it->
            titleEditText.setText(it[0])
            descriptionEditText.setText(it[1])
        }
        presenter.dataForFragmentResult.observe(requireActivity()){
            parentFragmentManager.setFragmentResult(
                NoteEdit::class.simpleName!!,
                it!!)
            parentFragmentManager.popBackStackImmediate()
        }
        (requireActivity() as Activity).fabButton.show()
        super.onViewCreated(view, savedInstanceState)
    }


    private fun initViews() {

        titleEditText = binding.titleEdittext
        descriptionEditText = binding.descriptionEdittext

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