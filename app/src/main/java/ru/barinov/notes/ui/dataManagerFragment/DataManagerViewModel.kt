package ru.barinov.notes.ui.dataManagerFragment

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.userRepository.NotesRepository
import ru.barinov.notes.domain.models.NoteEntity
import ru.barinov.notes.ui.dialogs.AgreementDialogFragment

class DataManagerViewModel(
    private val repository: NotesRepository,
    private val cloudDataBase: CloudRepository,
    private val auth: FirebaseAuth,
) : ViewModel() {

    private val _onRepositoryDeletion = MutableLiveData<DialogFragment>()
    val onRepositoryDeletion: LiveData<DialogFragment> = _onRepositoryDeletion

    private val _repositoryIsCleanedMessage = MutableLiveData<Unit>()
    val repositoryIsCleanedMessage: LiveData<Unit> = _repositoryIsCleanedMessage

    private val _hasCurrentUser = MutableLiveData<Boolean>()
    val hasCurrentUser: LiveData<Boolean> = _hasCurrentUser

    fun deleteAllNotes() {
        val confirmation = AgreementDialogFragment()
        _onRepositoryDeletion.postValue(confirmation)
    }

    fun onRepoDeletion(result: Bundle, key: String) {
        if (result.getBoolean(key)) {
            if (auth.currentUser != null) {
                repository.getNotesLiveData().observeForever { notes ->
                    Thread {
                        notes.forEach { note ->
                            cloudDataBase.deleteNoteInCloud(note)
                        }
                    }.start()
                }
            }
        }
        repository.removeAllNotes()
        _repositoryIsCleanedMessage.postValue(Unit)
    }

    fun checkOnCurrentUser() {
        var isUserOnline = cloudDataBase.auth.currentUser != null
        _hasCurrentUser.postValue(isUserOnline)

    }
}
