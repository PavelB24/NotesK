package ru.barinov.notes.ui.dataManagerFragment

import com.google.android.material.switchmaterial.SwitchMaterial
import ru.barinov.R
import ru.barinov.notes.domain.NotesRepository
import ru.barinov.notes.ui.Application


class DataManagerFragmentPresenter: DataManagerFragmentContract.DataManagerFragmentPresenterInterface {
    private  var view: DataManagerFragmentContract.ViewInterface? = null
    private lateinit var  repository: NotesRepository

    override fun onAttach(view: DataManagerFragment) {
        this.view= view
        repository= (view.requireActivity().application as Application).repository
    }

    override fun onDetach() {
        view= null
    }

    override fun deleteAllNotes() {
        repository.deleteAll()
        view?.onDeletedMessage()
    }

    override fun onSwitchListener(switchMaterial: SwitchMaterial) {
        switchMaterial.setOnClickListener {
            if (switchMaterial.isChecked)
            {view?.cloudStorageToast()
            }
            else { view?.localStorageToast()
            }
        }
    }
}