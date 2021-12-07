package ru.barinov.notes.ui.notesActivity

import android.os.Bundle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import ru.barinov.R
import ru.barinov.notes.domain.Authentication
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.curentDataBase.NotesRepository

import ru.barinov.notes.domain.noteEntityAndService.NoteEntity
import ru.barinov.notes.domain.room.DataBase

import ru.barinov.notes.ui.dataManagerFragment.DataManager
import java.io.IOException

class ActivityViewModel(
    repository: NotesRepository,
    database: DataBase,
    authentication: Authentication,
    cloudDataBase: CloudRepository,
) : ActivityContract.NoteActivityPresenterInterface {
    private val repository = repository
    private val LOCAL_REPOSITORY_NAME = "repository.bin"
    private val localDataBase = database
    private val authentication = authentication
    private val cloudDataBase = cloudDataBase


    private val  _onChooseStartFragment = MutableLiveData<Int>()
    val onChooseStartFragment: LiveData<Int> = _onChooseStartFragment


    @Throws(IOException::class)
    override fun safeNotes() {
//        val fos = view?.openFileOutput(LOCAL_REPOSITORY_NAME, MODE_PRIVATE)
//        val objectOutputStream = ObjectOutputStream(fos)
//        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//        val jsonAdapter = moshi.adapter(NoteEntity::class.java)
//        objectOutputStream.writeInt(repository.allNotes.size)
//        for (note in repository.allNotes) {
//            val json = jsonAdapter.toJson(note)
//            objectOutputStream.writeObject(json)
//        }
//        objectOutputStream.close()
//        fos?.close()
//        Log.d("@@@", "Записан")
    }


    override fun readNotes() {
        if (repository.allNotes.isEmpty()) {
            toInitNotesInRepository()

        }
    }

    fun readNotesFromCloud(result: Bundle?){
        if (result!!.getBoolean(DataManager::class.simpleName!!)) {
            toInitNotesFromCloud()
        }
    }

    override fun onChoseNavigationItem() {
        TODO("Not yet implemented")
    }

    override fun toInitNotesInRepository() {
        repository.addAll(localDataBase.noteDao().getAllNotes())


        //Старый метод, шпаргалка по Moshi
//        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//        val jsonAdapter = moshi.adapter(NoteEntity::class.java)
//        val fileInputStream = view?.openFileInput(LOCAL_REPOSITORY_NAME)
//        val objectInputStream = ObjectInputStream(fileInputStream)
//        val size = objectInputStream.readInt()
//        val list: MutableList<NoteEntity> = ArrayList()
//        for (i in 0 until size) {
//            val json: String = objectInputStream.readObject() as String
//            list.add(jsonAdapter.fromJson(json) as NoteEntity)
//        }
//        repository.addAll(list)
//        objectInputStream.close()
//        fileInputStream!!.close()
    }


    override fun toInitNotesFromCloud() {
        if (authentication.auth.currentUser != null) {
            Thread {
                cloudDataBase.cloud.collection(authentication.auth.currentUser?.uid.toString())
                    .get().addOnSuccessListener { result ->
                        for (document in result) {
                            val note = document.toObject(NoteEntity::class.java)
                            if (!repository.findById(note.id)) {
                                repository.addNote(note)
                            }
                        }
                    }
            }.start()
        }
    }



     fun chooseStartFragment() {
        if (authentication.auth.currentUser != null) {
            _onChooseStartFragment.postValue(R.id.notes_item_menu)
        } else {
            _onChooseStartFragment.postValue(R.id.profile_item_menu)
        }
    }

    fun logOut() {
        Thread {
            authentication.auth.signOut()
        }.start()
        authentication.isOnline = false
    }
}