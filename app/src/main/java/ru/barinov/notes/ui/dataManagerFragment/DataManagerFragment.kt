package ru.barinov.notes.ui.dataManagerFragment

import android.content.Context
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
import ru.barinov.notes.ui.notesActivity.NotesActivity
import android.content.SharedPreferences




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
        val prefs = requireActivity().getSharedPreferences("safe", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        (requireActivity() as NotesActivity).bottomNavigationItemView.setBackgroundColor(resources.getColor(R.color.toolbar_grey))
        presenter.onAttach(this)
        deleteImageButton= binding.deleteStorageButton
        initDeleteButton()
        switchMaterial= binding.dataManagerSwitch
        presenter.onSwitchListener(switchMaterial)
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

    override fun onDestroy() {
        presenter.savePref(switchMaterial)
        super.onDestroy()
    }
}


