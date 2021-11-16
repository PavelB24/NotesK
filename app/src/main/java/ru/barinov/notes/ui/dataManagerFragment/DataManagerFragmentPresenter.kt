package ru.barinov.notes.ui.dataManagerFragment

import com.google.android.material.switchmaterial.SwitchMaterial
import ru.barinov.notes.domain.NotesRepository
import ru.barinov.notes.ui.Application
import ru.barinov.notes.ui.application


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

    override fun deleteAllNotes(app: Application) {
        repository.deleteAll()
        Thread{
        app.dataBase.clearAllTables()}.start()
        view?.onDeletedMessage()
    }

    override fun onSwitchListener(switchMaterial: SwitchMaterial) {
        switchMaterial.setOnClickListener {
            if (switchMaterial.isChecked)
            {view?.cloudStorageText()
            }
            else { view?.localStorageText()
            }
        }
    }
}