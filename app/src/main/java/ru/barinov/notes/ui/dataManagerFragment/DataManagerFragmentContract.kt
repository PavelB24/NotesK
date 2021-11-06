package ru.barinov.notes.ui.dataManagerFragment

import com.google.android.material.switchmaterial.SwitchMaterial

class DataManagerFragmentContract {
    interface DataManagerFragmentPresenterInterface{
        fun onAttach(view: DataManagerFragment)
        fun onDetach()

        fun deleteAllNotes()
        fun onSwitchListener(switchMaterial: SwitchMaterial)
    }
    interface ViewInterface{

       fun onDeletedMessage()
       fun cloudStorageToast()
       fun localStorageToast()
    }
}