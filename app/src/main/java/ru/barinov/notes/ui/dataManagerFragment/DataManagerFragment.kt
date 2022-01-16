package ru.barinov.notes.ui.dataManagerFragment

import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.inject
import ru.barinov.R
import ru.barinov.databinding.DataManagerLayoutBinding
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.userRepository.NotesRepository
import ru.barinov.notes.domain.room.DataBase
import ru.barinov.notes.ui.dialogs.AgreementDialogFragment
import ru.barinov.notes.ui.application

private const val DELETE = "OK"

class DataManagerFragment : Fragment() {

    private val viewModel by viewModel<DataManagerViewModel>()
    private lateinit var binding: DataManagerLayoutBinding
    private lateinit var deleteImageButton: ImageButton
    private lateinit var switchMaterial: SwitchMaterial
    private lateinit var editor: SharedPreferences.Editor
    private val sharedPreferences = inject<SharedPreferences>(SharedPreferences::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        editor = sharedPreferences.value.edit()
        binding = DataManagerLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deleteImageButton = binding.deleteStorageButton
        switchMaterial = binding.dataManagerSwitch
        initDeleteButton()
        setOnSwitchListener()
        getDialogResults()
    }

    private fun getDialogResults() {
        viewModel.onRepositoryDeletion.observe(viewLifecycleOwner) { it ->
            it.show(parentFragmentManager, DELETE)
            parentFragmentManager.setFragmentResultListener(AgreementDialogFragment::class.simpleName!!,
                requireActivity(),
                { requestKey, result ->
                    viewModel.onRepoDeletion(result, DELETE)
                })
        }
        viewModel.repositoryIsCleanedMessage.observe(viewLifecycleOwner) {
            onDeletedMessage()
        }
    }

    private fun initDeleteButton() {
        deleteImageButton.setOnClickListener {
            viewModel.deleteAllNotes()
        }
    }

    private fun onDeletedMessage() {
        Toast.makeText(activity, getString(R.string.all_notes_deleted_text), Toast.LENGTH_SHORT).show()
    }

    private fun cloudStorageText() {
        binding.dataManagerSwitchTextView.setText(R.string.way_of_storage_cloud)
        Toast.makeText(
            context, getString(R.string.local_and_cloud_storage_text), Toast.LENGTH_SHORT
        ).show()
    }

    private fun localStorageText() {
        binding.dataManagerSwitchTextView.setText(R.string.way_of_storage_local)
        Toast.makeText(
            context, getString(R.string.only_local_storage_text), Toast.LENGTH_SHORT
        ).show()
    }

    private fun showCloudIsNotAvailableMassage() {
        Toast.makeText(
            context, getString(R.string.ask_for_log_in_text), Toast.LENGTH_SHORT
        ).show()
    }

    private fun setOnSwitchListener() {

        viewModel.checkOnCurrentUser()
        viewModel.hasCurrentUser.observe(viewLifecycleOwner) { isAuthenticated ->
            val checkedPreferences = sharedPreferences.value.getBoolean(
                switchStateKey, false
            )
            switchMaterial.isChecked = !(!isAuthenticated || !checkedPreferences)

        }


        switchMaterial.setOnClickListener {
            viewModel.checkOnCurrentUser()
            viewModel.hasCurrentUser.observe(viewLifecycleOwner) { isAuthenticated ->
                if (switchMaterial.isChecked) {
                    if (isAuthenticated) {
                        cloudStorageText()
                        savePref()
                    } else {
                        switchMaterial.isChecked = false
                        showCloudIsNotAvailableMassage()
                        savePref()
                    }
                } else {
                    localStorageText()
                    savePref()
                }
            }
        }
    }

    private fun savePref() {
        editor.putBoolean(switchStateKey, switchMaterial.isChecked).apply()
    }

    companion object {

        const val sharedPreferencesName = "AppSettings"
        const val switchStateKey = "SwitchState"
    }

}


