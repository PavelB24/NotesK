package ru.barinov.notes.ui.dataManagerFragment

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.userRepository.NotesRepository
import ru.barinov.notes.domain.entity.NoteEntity
import ru.barinov.notes.ui.dialogs.AgreementDialogFragment

class DataManagerViewModel(
    private val repository: NotesRepository,
    private val cloudDataBase: CloudRepository,
    private val auth: FirebaseAuth,
) {

    private val _onRepositoryDeletion = MutableLiveData<DialogFragment>()
    val onRepositoryDeletion: LiveData<DialogFragment> = _onRepositoryDeletion

    private val _repositoryIsCleanedMessage = MutableLiveData<Unit>()
    val repositoryIsCleanedMessage: LiveData<Unit> = _repositoryIsCleanedMessage

    fun deleteAllNotes() {
        val confirmation = AgreementDialogFragment()
        _onRepositoryDeletion.postValue(confirmation)
    }

    fun onRepoDeletion(result: Bundle, key: String) {
        if (result.getBoolean(key)) {
            if (auth.currentUser != null) {
                var deletionList: List<NoteEntity> = listOf()
                repository.getNotesLiveData().observeForever {notes->
                    deletionList= notes
                }
                Thread {
                    deletionList.forEach {
                        cloudDataBase.cloud.collection(auth.currentUser!!.uid).document(it.id).delete()
                    }
                }.start()
            }
        }
        repository.removeAllNotes()
        _repositoryIsCleanedMessage.postValue(Unit)
    }
}
