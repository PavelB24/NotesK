package ru.barinov.notes.ui.dataManagerFragment


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.room.DataBase


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

    private val _liveData = MutableLiveData<Boolean>()
    override val ld: LiveData<Boolean> = _liveData

    override fun deleteAllNotes() {
        repository.deleteAll()
        Thread {
            localDB.clearAllTables()
        }.start()
        if (auth.currentUser != null) {
            //ToDo}
        }
        _liveData.postValue(true)
    }
}