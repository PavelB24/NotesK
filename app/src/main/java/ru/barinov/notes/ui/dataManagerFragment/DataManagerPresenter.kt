package ru.barinov.notes.ui.dataManagerFragment

import android.content.Context
import android.content.SharedPreferences
import com.google.android.material.switchmaterial.SwitchMaterial
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.ui.Application


class DataManagerPresenter: DataManagerContract.DataManagerFragmentPresenterInterface {
    private  var view: DataManager? = null
    private lateinit var  repository: NotesRepository
    private lateinit  var  pref: SharedPreferences
    private lateinit var  editor: SharedPreferences.Editor

    override fun onAttach(view: DataManager) {
        this.view= view
        pref= view.requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        editor= pref.edit()
        repository= (view.requireActivity().application as Application).repository
    }

    override fun onDetach() {
        view= null
    }

    override fun deleteAllNotes(app: Application) {
        repository.deleteAll()
        Thread{
        app.localDataBase.clearAllTables()}.start()
        view?.onDeletedMessage()
    }

    override fun onSwitchListener(switchMaterial: SwitchMaterial) {
        switchMaterial.isChecked= pref.getBoolean("switchState", false)
        switchMaterial.setOnClickListener {
            if (switchMaterial.isChecked)
            {view?.cloudStorageText()
            }
            else { view?.localStorageText()
            }
            savePref(switchMaterial)
        }
    }

    fun savePref(switchMaterial: SwitchMaterial){
        editor.putBoolean("switchState", switchMaterial.isChecked ).apply()
    }
}