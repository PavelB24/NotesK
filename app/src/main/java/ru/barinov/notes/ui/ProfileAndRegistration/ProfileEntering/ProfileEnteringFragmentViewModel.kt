package ru.barinov.notes.ui.ProfileAndRegistration.ProfileEntering

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.models.NoteEntity
import ru.barinov.notes.domain.userRepository.NotesRepository
import ru.barinov.notes.ui.ProfileAndRegistration.AuthenticationDataDraft

private const val loginSharedPreferencesKey = "Login"
private const val passwordSharedPreferencesKey = "Password"

class ProfileEnteringFragmentViewModel(
    private val sharedPreferences: SharedPreferences,
    private val repository: NotesRepository,
    private val cloudRepository: CloudRepository

) : ViewModel() {

    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    private val _authenticationDataRestored = MutableLiveData<AuthenticationDataDraft>()
    val authenticationDataRestored: LiveData<AuthenticationDataDraft> = _authenticationDataRestored

    private val _onSuccessfulEnter = MutableLiveData<Unit>()
    val onSuccessfulEnter: LiveData<Unit> = _onSuccessfulEnter

    private val _onUnSuccessfulEnter = MutableLiveData<Unit>()
    val onUnSuccessfulEnter: LiveData<Unit> = _onUnSuccessfulEnter

    private val _onOneOrMoreFieldsAreEmpty = MutableLiveData<Unit>()
    val onOneOrMoreFieldsAreEmpty: LiveData<Unit> = _onOneOrMoreFieldsAreEmpty

    fun restoreLastAuthenticationData() {
        val login = sharedPreferences.getString(loginSharedPreferencesKey, "")!!
        val password = sharedPreferences.getString(passwordSharedPreferencesKey, "")!!
        _authenticationDataRestored.postValue(AuthenticationDataDraft(login, password))

    }

    fun saveFields(draft: AuthenticationDataDraft) {
        editor.putString(loginSharedPreferencesKey, draft.login).apply()
        editor.putString(passwordSharedPreferencesKey, draft.password).apply()
    }

    fun onEnterButtonPressed(draft: AuthenticationDataDraft) {
        if (draft.login.isNotEmpty() && draft.password.isNotEmpty()) {
            saveFields(draft)
            Thread {
                cloudRepository.auth.signInWithEmailAndPassword(
                    draft.login, draft.password
                ).addOnCompleteListener { answer ->
                    if (answer.isSuccessful) {
                        synchronizeRepos()
                        _onSuccessfulEnter.postValue(Unit)
                    } else {
                        _onUnSuccessfulEnter.postValue(Unit)

                    }
                }
            }.start()
        } else {
            _onOneOrMoreFieldsAreEmpty.postValue(Unit)
        }
    }

    private fun synchronizeRepos() {
        if (cloudRepository.auth.currentUser != null) {
            val authentication = cloudRepository.auth
            Thread {
                cloudRepository.cloud.collection(authentication.currentUser?.uid.toString()).get()
                    .addOnSuccessListener { result ->
                        Log.d("@@@", "синхронизирует")
                        for (document in result) {
                            val note = document.toObject(NoteEntity::class.java)
                            if (!repository.findById(note.id)) {
                                repository.insertNote(note)
                            }
                        }
                    }
            }.start()
        }
    }
}