package ru.barinov.notes.ui.dataManagerFragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import ru.barinov.R
import ru.barinov.databinding.DataManagerLayoutBinding
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.room.DataBase
import ru.barinov.notes.ui.dialogs.AgreementDialogFragment
import ru.barinov.notes.ui.application
import ru.barinov.notes.ui.notesActivity.Activity


class DataManager: Fragment() {
    private lateinit var binding: DataManagerLayoutBinding
    private lateinit var deleteImageButton: ImageButton
    private lateinit var switchMaterial: SwitchMaterial
    private lateinit var viewModel: DataManagerViewModel
    private lateinit var  editor: SharedPreferences.Editor
    private val DELETE = "OK"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= DataManagerLayoutBinding.inflate(inflater)
        viewModel= DataManagerViewModel(getRepository(), getLocalDB(), getCloudDB(), requireActivity().application().authentication.auth)
        requireActivity().window.statusBarColor= activity?.resources!!.getColor(R.color.deep_blue_2)
        requireActivity().window.navigationBarColor= activity?.resources!!.getColor(R.color.card_view_grey_2)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as Activity).bottomAppBar.backgroundTint = ContextCompat.getColorStateList(requireContext(), R.color.card_view_grey)
        editor= getSharedPreferences().edit()
        deleteImageButton= binding.deleteStorageButton
        initDeleteButton()
        switchMaterial= binding.dataManagerSwitch
        setOnSwitchListener()
        getDialogResults()
        }

    private fun getDialogResults() {
        viewModel.onRepositoryDeletion.observe(requireActivity()) { it ->
            it.show(parentFragmentManager, DELETE)
            parentFragmentManager.setFragmentResultListener(
                AgreementDialogFragment::class.simpleName!!,
                requireActivity(), { requestKey, result ->
                    viewModel.onRepoDeletion(result, DELETE)
                })
        }
        viewModel.repositoryIsCleanedMessage.observe(requireActivity()){
            onDeletedMessage()
        }
    }


    private fun getRepository():NotesRepository {
        return requireActivity().application().repository
    }

    private fun getLocalDB(): DataBase {
        return requireActivity().application().localDataBase
    }
    private fun getCloudDB(): CloudRepository {
        return requireActivity().application().cloudDataBase
    }


    private fun initDeleteButton() {
        deleteImageButton.setOnClickListener {
           viewModel.deleteAllNotes()
        }
    }

    private fun onDeletedMessage() {
        Toast.makeText(activity, "Notes Deleted", Toast.LENGTH_SHORT).show()
    }

    private fun cloudStorageText() {
        binding.dataManagerSwitchTextView.setText(R.string.way_of_storage_cloud)
        Toast.makeText(context, "Local and cloud storage", Toast.LENGTH_SHORT).show()
    }

    private fun localStorageText() {
        binding.dataManagerSwitchTextView.setText(R.string.way_of_storage_local)
        Toast.makeText(context, "Only local storage", Toast.LENGTH_SHORT).show()
    }

    private fun showCloudIsNotAvailableMassage(){
        Toast.makeText(context, "Please, log in to use cloud storage", Toast.LENGTH_SHORT).show()
    }
    private fun setOnSwitchListener() {
        switchMaterial.isChecked= getSharedPreferences().getBoolean(DataManager.switchStateKey, false)
        if (getSharedPreferences().getBoolean(DataManager.switchStateKey, false)){
            binding.dataManagerSwitchTextView.setText(R.string.way_of_storage_cloud)
        }
        if(requireActivity().application().authentication.auth.currentUser== null){
            switchMaterial.isChecked = false
        }
        switchMaterial.setOnClickListener {
            if (switchMaterial.isChecked) {
                if (requireActivity().application().authentication.auth.currentUser != null) {
                    cloudStorageText()
                    savePref()
                }
                else {
                    switchMaterial.isChecked= false
                    savePref()
                    showCloudIsNotAvailableMassage()
                }
            }
            else {
               localStorageText()
                savePref()
            }

        }
    }
    private fun savePref(){
        editor.putBoolean(DataManager.switchStateKey, switchMaterial.isChecked ).apply()
    }

    private fun getSharedPreferences(): SharedPreferences{
       return requireActivity().application().pref
    }

    companion object{
        const val switchStateKey = "SwitchState"
    }



}


