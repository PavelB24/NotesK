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

class DataManagerFragment: Fragment(), DataManagerFragmentContract.ViewInterface {
    private lateinit var binding: DataManagerLayoutBinding
    private lateinit var deleteImageButton: ImageButton
    private lateinit var switchMaterial: SwitchMaterial
    private lateinit var presenter: DataManagerFragmentPresenter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= DataManagerLayoutBinding.inflate(inflater)
        presenter= DataManagerFragmentPresenter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        deleteImageButton= binding.deleteStorageButton
        initDeleteButton()
        switchMaterial= binding.dataManagerSwitch
        presenter.onSwitchListener(switchMaterial)
    }


    private fun initDeleteButton() {
        //Todo переписаьть под диалог с выбором где удалить заметки
        deleteImageButton.setOnClickListener {
           presenter.deleteAllNotes()
        }
    }

    private fun initDataSwitch() {
        switchMaterial= binding.dataManagerSwitch
        switchMaterial.setOnClickListener {
            if (switchMaterial.isChecked)
            {binding.dataManagerSwitchTextView.setText(R.string.way_of_storage_cloud)
            } else { binding.dataManagerSwitchTextView.setText(R.string.way_of_storage_local)
            }
        }
    }


    override fun onDeletedMessage() {
        Toast.makeText(activity, "Notes Deleted", Toast.LENGTH_SHORT).show()
    }

    override fun cloudStorageToast() {
        binding.dataManagerSwitchTextView.setText(R.string.way_of_storage_cloud)
    }

    override fun localStorageToast() {
        binding.dataManagerSwitchTextView.setText(R.string.way_of_storage_local)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}


