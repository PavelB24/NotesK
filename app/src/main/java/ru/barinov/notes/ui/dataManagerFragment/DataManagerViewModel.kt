package ru.barinov.notes.ui.dataManagerFragment


import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.room.DataBase
import ru.barinov.notes.ui.AgreementDialogFragment


class DataManagerViewModel(
    repository: NotesRepository,
    localDB: DataBase,
    cloudDataBase: CloudRepository,
    auth: FirebaseAuth
) :
    DataManagerContract.DataManagerFragmentPresenterInterface {
    private val repository: NotesRepository = repository
    private val localDB: DataBase = localDB
    private val cloudDataBase: CloudRepository = cloudDataBase
    private val auth = auth

    private val _onRepositoryDeletion = MutableLiveData<DialogFragment>()
    override val onRepositoryDeletion: LiveData<DialogFragment> = _onRepositoryDeletion

    private val _repositoryIsCleanedMessage = MutableLiveData<Boolean>()
    override val repositoryIsCleanedMessage: LiveData<Boolean> = _repositoryIsCleanedMessage


    override fun deleteAllNotes() {
        val confirmation = AgreementDialogFragment()
        _onRepositoryDeletion.postValue(confirmation)
    }

    fun onRepoDeletion(result: Bundle, key: String) {

        if (result.getBoolean(key)) {
            repository.deleteAll()
            Thread {
                localDB.clearAllTables()
            }.start()
            if (auth.currentUser != null) {
                //ToDo}
            }
            _repositoryIsCleanedMessage.postValue(true)
        }
    }
}