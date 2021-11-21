package ru.barinov.notes.ui.dataManagerFragment

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
import ru.barinov.notes.ui.notesActivity.Activity


class DataManager: Fragment(), DataManagerContract.ViewInterface {
    private lateinit var binding: DataManagerLayoutBinding
    private lateinit var deleteImageButton: ImageButton
    private lateinit var switchMaterial: SwitchMaterial
    private lateinit var presenter: DataManagerPresenter


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
        presenter.onAttach(this)
        deleteImageButton= binding.deleteStorageButton
        initDeleteButton()
        switchMaterial= binding.dataManagerSwitch
        presenter.onSwitchListener(switchMaterial)
        if(switchMaterial.isChecked){
            binding.dataManagerSwitchTextView.setText(R.string.way_of_storage_cloud)

        }
    }


    private fun initDeleteButton() {
        //Todo переписаьть под диалог с выбором где удалить заметки
        deleteImageButton.setOnClickListener {
           presenter.deleteAllNotes((requireActivity().application as Application))
        }
    }



    override fun onDeletedMessage() {
        Toast.makeText(activity, "Notes Deleted", Toast.LENGTH_SHORT).show()
    }

    override fun cloudStorageText() {
        binding.dataManagerSwitchTextView.setText(R.string.way_of_storage_cloud)
    }

    override fun localStorageText() {
        binding.dataManagerSwitchTextView.setText(R.string.way_of_storage_local)
    }

}


