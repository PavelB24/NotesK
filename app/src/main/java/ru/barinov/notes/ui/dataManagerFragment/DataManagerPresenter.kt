package ru.barinov.notes.ui.dataManagerFragment


import com.google.firebase.auth.FirebaseAuth
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.room.DataBase


class DataManagerPresenter: DataManagerContract.DataManagerFragmentPresenterInterface {
    private  var view: DataManagerContract.ViewInterface? = null
    private lateinit var  repository: NotesRepository
    private lateinit var  localDB: DataBase
    private lateinit var  cloudDataBase: CloudRepository


    override fun onAttach(
        view: DataManagerContract.ViewInterface,
        repository: NotesRepository,
        localDB: DataBase,
        cloudDataBase: CloudRepository
    ) {
        this.cloudDataBase = cloudDataBase
        this.view= view
        this.repository = repository
    }

    override fun onDetach() {
        view= null
    }

    override fun deleteAllNotes(auth: FirebaseAuth) {
        repository.deleteAll()
        Thread {
            localDB.clearAllTables()
        }.start()
        if (auth.currentUser != null) {
            //ToDo}
            view?.onDeletedMessage()
        }
    }
}