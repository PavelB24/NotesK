package ru.barinov.notes.ui.notesActivity

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import ru.barinov.R

import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.userRepository.NotesRepository

import ru.barinov.notes.domain.models.NoteEntity

import ru.barinov.notes.ui.ProfileAndRegistration.Logged.LoggedFragment
import ru.barinov.notes.ui.ProfileAndRegistration.ProfileEntering.ProfileEnteringFragment
import java.io.IOException

class ActivityViewModel(
    private val repository: NotesRepository, private val cloudDataBase: CloudRepository
) : ViewModel() {

    private val LOCAL_REPOSITORY_NAME = "repository.bin"

    private val _onChooseStartFragment = MutableLiveData<Int>()
    val onChooseStartFragment: LiveData<Int> = _onChooseStartFragment

    private val _onCloudInitCompleted = MutableLiveData<Unit>()
    val onCloudInitCompleted: LiveData<Unit> = _onCloudInitCompleted

    private val _hasLoggedUser = MutableLiveData<Boolean>()
    val hasLoggedUser: LiveData<Boolean> = _hasLoggedUser

    val noesListSize = repository.getNotesLiveData()

    @Throws(IOException::class)
    fun safeNotes() {
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

    fun readNotesFromCloud() {
        if (cloudDataBase.auth.currentUser != null) {
            Thread {
                cloudDataBase.cloud.collection(cloudDataBase.auth.currentUser!!.uid).get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val note = document.toObject(NoteEntity::class.java)
                            if (!repository.findById(note.id)) {
                                repository.insertNote(note)
                            }
                        }
                        _onCloudInitCompleted.postValue(Unit)
                    }
            }.start()
        }
    }

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

    fun chooseStartFragment() {
        if (cloudDataBase.auth.currentUser != null) {
            _onChooseStartFragment.postValue(R.id.notes_item_menu)
        } else {
            _onChooseStartFragment.postValue(R.id.profile_item_menu)
        }
    }

    fun logOut() {
        Thread {
            cloudDataBase.auth.signOut()
        }.start()
    }

    fun checkOnUserOnline() {
        _hasLoggedUser.postValue(cloudDataBase.auth.currentUser != null)
    }

}