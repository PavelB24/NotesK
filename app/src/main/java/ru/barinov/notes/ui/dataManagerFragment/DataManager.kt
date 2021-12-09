package ru.barinov.notes.ui.dataManagerFragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import ru.barinov.R
import ru.barinov.databinding.DataManagerLayoutBinding
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.room.DataBase
import ru.barinov.notes.ui.AgreementDialogFragment
import ru.barinov.notes.ui.application
import ru.barinov.notes.ui.notesActivity.Activity


class DataManager: Fragment() {
    private lateinit var binding: DataManagerLayoutBinding
    private lateinit var deleteImageButton: ImageButton
    private lateinit var switchMaterial: SwitchMaterial
    private lateinit var presenter: DataManagerViewModel
    private lateinit  var  pref: SharedPreferences
    private lateinit var  editor: SharedPreferences.Editor
    private val switchStateKey = "SwitchState"
    private val DELETE = "OK"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= DataManagerLayoutBinding.inflate(inflater)
        presenter= DataManagerViewModel(getRepository(), getLocalDB(), getCloudDB(), requireActivity().application().authentication.auth)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as Activity).bottomNavigationItemView.setBackgroundColor(resources.getColor(R.color.cherry))
        pref= requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        editor= pref.edit()
        deleteImageButton= binding.deleteStorageButton
        initDeleteButton()
        switchMaterial= binding.dataManagerSwitch
        setOnSwitchListener()
        getDialogResults()
        }

    private fun getDialogResults() {
        presenter.onRepositoryDeletion.observe(requireActivity()) { it ->
            it.show(parentFragmentManager, DELETE)
            parentFragmentManager.setFragmentResultListener(
                AgreementDialogFragment::class.simpleName!!,
                requireActivity(), { requestKey, result ->
                    presenter.onRepoDeletion(result, DELETE)
                })
        }
        presenter.repositoryIsCleanedMessage.observe(requireActivity()){
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
        //Todo переписаьть под диалог с выбором где удалить заметки
        deleteImageButton.setOnClickListener {
           presenter.deleteAllNotes()
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
        switchMaterial.isChecked= pref.getBoolean(switchStateKey, false)
        if (pref.getBoolean(switchStateKey, false)){
            binding.dataManagerSwitchTextView.setText(R.string.way_of_storage_cloud)
        }
        if(requireActivity().application().authentication.auth.currentUser== null){
            switchMaterial.isChecked = false
        }
        switchMaterial.setOnClickListener {
            if (switchMaterial.isChecked) {
                if (requireActivity().application().authentication.auth.currentUser != null) {
                    cloudStorageText()
                    parentFragmentManager.setFragmentResult(DataManager::class.simpleName!!,
                        Bundle().also { it.putBoolean(DataManager::class.simpleName!!, true) })
                }
                else {
                    switchMaterial.isChecked= false
                    parentFragmentManager.setFragmentResult(DataManager::class.simpleName!!,
                        Bundle().also { it.putBoolean(DataManager::class.simpleName!!, false) })
                    showCloudIsNotAvailableMassage()
                }
            }
            else {
               localStorageText()
                parentFragmentManager.setFragmentResult(DataManager::class.simpleName!!,
                    Bundle().also { it.putBoolean(DataManager::class.simpleName!!, false) })

            }
            savePref()
        }
    }
    private fun savePref(){
        editor.putBoolean(switchStateKey, switchMaterial.isChecked ).apply()
    }



}


