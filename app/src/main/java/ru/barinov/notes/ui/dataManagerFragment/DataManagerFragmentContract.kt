package ru.barinov.notes.ui.dataManagerFragment

import com.google.android.material.switchmaterial.SwitchMaterial
import ru.barinov.notes.ui.Application

class DataManagerFragmentContract {
    interface DataManagerFragmentPresenterInterface{
        fun onAttach(view: DataManagerFragment)
        fun onDetach()

        fun deleteAllNotes(app: Application)
        fun onSwitchListener(switchMaterial: SwitchMaterial)
    }
    interface ViewInterface{

       fun onDeletedMessage()
       fun cloudStorageText()
       fun localStorageText()
    }
}