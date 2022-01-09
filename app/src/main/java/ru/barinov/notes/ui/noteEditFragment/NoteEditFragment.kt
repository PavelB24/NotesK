package ru.barinov.notes.ui.noteEditFragment

import android.Manifest
import android.content.*
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.barinov.R
import ru.barinov.databinding.NoteEditLayoutBinding
import ru.barinov.notes.domain.ViewModelsFactories
import ru.barinov.notes.ui.application
import ru.barinov.notes.ui.dataManagerFragment.DataManagerFragment
import ru.barinov.notes.ui.notesActivity.ActivityMain

const val argsBundleKey = "NotesId"

class NoteEditFragment() : Fragment() {

    private lateinit var applyButton: Button
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var binding: NoteEditLayoutBinding
    private lateinit var viewModel: NoteEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = NoteEditLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initViews()
        val permission = ActivityCompat.checkSelfPermission(
            requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PERMISSION_GRANTED
        val tempNoteId = requireArguments().getString(argsBundleKey)
        viewModel = ViewModelProvider(
            viewModelStore, ViewModelsFactories.NoteEditViewModelFactory(
                id = tempNoteId,
                repository = requireActivity().application().repository,
                locationFinder = requireActivity().application().locationFinder,
                permission = permission,
                cloudRepository = requireContext().application().cloudDataBase,
                sharedPreferences = requireContext().getSharedPreferences(DataManagerFragment.switchStateKey, Context.MODE_PRIVATE)
            )
        ).get(NoteEditViewModel::class.java)

        viewModel.startListenLocation()
        viewModel.fillTheViews()
        registeredForViewContentLiveData()

        initButton()
        viewModel.fieldsIsNotFilledMassageLiveData.observe(requireActivity()) {
            Toast.makeText(activity, R.string.warning_toast, Toast.LENGTH_SHORT).show()
        }

        viewModel.closeScreenViewModel.observe(requireActivity()) {
            parentFragmentManager.popBackStackImmediate()
        }
        (requireActivity() as ActivityMain).fabButton.show()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun registeredForViewContentLiveData() {
        viewModel.viewContentLiveData.observe(viewLifecycleOwner) {
            titleEditText.setText(it.title)
            contentEditText.setText(it.content)
        }
    }

    private fun initViews() {

        titleEditText = binding.titleEdittext
        contentEditText = binding.descriptionEdittext

    }

    private fun initButton() {
        applyButton = binding.applyButton as Button
        applyButton.setOnClickListener {
            viewModel.saveNote(
                draft = NoteDraft(
                    titleEditText.text.toString(), contentEditText.text.toString()
                )
            )
        }
    }

    override fun onPause() {
        parentFragmentManager.popBackStack()
        super.onPause()
    }

    override fun onDestroy() {
        viewModel.removeLocationListener()
        super.onDestroy()
    }

    companion object {

        fun getInstance(id: String?): NoteEditFragment {
            val fragment = NoteEditFragment()
            val data = Bundle()
            data.putString(argsBundleKey, id)
            fragment.arguments = data
            return fragment
        }
    }

}