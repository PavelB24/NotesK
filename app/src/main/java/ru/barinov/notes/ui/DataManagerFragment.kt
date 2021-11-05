package ru.barinov.notes.ui

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
import ru.barinov.notes.domain.NotesRepository

class DataManagerFragment: Fragment() {
    private lateinit var binding: DataManagerLayoutBinding
    private lateinit var deleteImageButton: ImageButton
    private lateinit var switchMaterial: SwitchMaterial
    private lateinit var repository: NotesRepository


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataManagerLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        deleteImageButton= binding.deleteStorageButton
        switchMaterial= binding.dataManagerSwitch
    }

    private fun initViews() {
        val data: Bundle? = arguments
        repository = data?.getParcelable<NotesRepository>(NotesRepository::class.simpleName)!!
        initDeleteButton()
        initDataSwitch()


    }

    private fun initDeleteButton() {
        //Todo переписаьть под диалог с выбором где удалить заметки
        deleteImageButton= binding.deleteStorageButton
        deleteImageButton.setOnClickListener {
            repository.deleteAll()
            Toast.makeText(activity, "Notes Deleted", Toast.LENGTH_SHORT).show()

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


    companion object {
        fun getInstance(data: Bundle?): DataManagerFragment {
            val dataManagerInstance = DataManagerFragment()
            dataManagerInstance.arguments = data
            return dataManagerInstance
        }
    }
}


