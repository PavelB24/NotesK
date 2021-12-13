package ru.barinov.notes.ui.dataManagerFragment

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.room.DataBase
import ru.barinov.notes.ui.Application

class DataManagerContract {
    interface DataManagerFragmentPresenterInterface{
        fun deleteAllNotes()
        val onRepositoryDeletion: LiveData<DialogFragment>
        val repositoryIsCleanedMessage: LiveData<Unit>
    }

}