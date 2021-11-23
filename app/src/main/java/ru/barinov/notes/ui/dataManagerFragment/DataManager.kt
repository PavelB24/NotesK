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
import ru.barinov.notes.ui.Application
import ru.barinov.notes.ui.application
import ru.barinov.notes.ui.notesActivity.Activity


class DataManager: Fragment(), DataManagerContract.ViewInterface {
    private lateinit var binding: DataManagerLayoutBinding
    private lateinit var deleteImageButton: ImageButton
    private lateinit var switchMaterial: SwitchMaterial
    private lateinit var presenter: DataManagerPresenter
    private lateinit  var  pref: SharedPreferences
    private lateinit var  editor: SharedPreferences.Editor
    private val switchStateKey = "SwitchState"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= DataManagerLayoutBinding.inflate(inflater)
        presenter= DataManagerPresenter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as Activity).bottomNavigationItemView.setBackgroundColor(resources.getColor(R.color.cherry))
        presenter.onAttach(this, requireActivity().application().repository,
            requireActivity().application().localDataBase,
            requireActivity().application().cloudDataBase)
        pref= requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        editor= pref.edit()
        deleteImageButton= binding.deleteStorageButton
        initDeleteButton()
        switchMaterial= binding.dataManagerSwitch
        setOnSwitchListener()
    }


    private fun initDeleteButton() {
        //Todo переписаьть под диалог с выбором где удалить заметки
        deleteImageButton.setOnClickListener {
           presenter.deleteAllNotes(requireActivity().application().authentication.auth)
        }
    }

    override fun onDeletedMessage() {
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
    fun setOnSwitchListener() {
        switchMaterial.isChecked= pref.getBoolean(switchStateKey, false)
        switchMaterial.setOnClickListener {
            if (switchMaterial.isChecked) {
                if ((requireActivity().application as Application).authentication.auth.currentUser != null) {
                    cloudStorageText()
                    parentFragmentManager.setFragmentResult(DataManager::class.simpleName!!,
                        Bundle().also { it.putBoolean(DataManager::class.simpleName!!, true) })
                }
                else {
                    switchMaterial.isChecked= false
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


