package ru.barinov.notes.ui.dataManagerFragment

import androidx.fragment.app.FragmentManager
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.room.DataBase
import ru.barinov.notes.ui.Application

class DataManagerContract {
    interface DataManagerFragmentPresenterInterface{
        fun onAttach(view: ViewInterface, repository: NotesRepository, localDB: DataBase, cloudDataBase: CloudRepository)
        fun onDetach()

        fun deleteAllNotes(auth: FirebaseAuth)
    }
    interface ViewInterface{

       fun onDeletedMessage()
    }
}