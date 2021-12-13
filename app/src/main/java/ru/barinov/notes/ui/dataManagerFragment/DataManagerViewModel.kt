package ru.barinov.notes.ui.dataManagerFragment


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.room.DataBase
import ru.barinov.notes.ui.dialogs.AgreementDialogFragment


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

    private val _repositoryIsCleanedMessage = MutableLiveData<Unit>()
    override val repositoryIsCleanedMessage: LiveData<Unit> = _repositoryIsCleanedMessage


    override fun deleteAllNotes() {
        val confirmation = AgreementDialogFragment()
        _onRepositoryDeletion.postValue(confirmation)
    }

    fun onRepoDeletion(result: Bundle, key: String) {
        if (result.getBoolean(key)) {
            Thread { localDB.clearAllTables() }.start()
        }
        if (auth.currentUser != null) {
            Log.d("@@@", "onRepoDeletion: ")
            val deletionList = repository.getNotes()
            Thread {
                deletionList.forEach {
                    cloudDataBase.cloud.collection(auth.currentUser!!.uid)
                        .document(it.id).delete()
                }
            }.start()
        }
        repository.deleteAll()
        _repositoryIsCleanedMessage.postValue(Unit)
    }
}
