package ru.barinov.notes.ui.ProfileAndRegistration.RegistrationFragment

import android.util.Log
import androidx.lifecycle.*
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.ui.ProfileAndRegistration.AuthenticationDataDraft

class RegistrationFragmentViewModel(private val cloudDataBase: CloudRepository) : ViewModel() {

    private val _onUnSuccessfulRegistration = MutableLiveData<Unit>()
    val onUnSuccessfulRegistration: LiveData<Unit> = _onUnSuccessfulRegistration

    private val _onSuccessfulRegistration = MutableLiveData<Unit>()
    val onSuccessfulRegistration: LiveData<Unit> = _onSuccessfulRegistration

    fun registerNewUser(draft: AuthenticationDataDraft) {
        cloudDataBase.auth.createUserWithEmailAndPassword(
            draft.login.trim(), draft.password.trim()
        ).addOnCompleteListener { answer ->
            if (answer.isSuccessful) {
                Log.d("@@@", "createUserWithEmail:success")
                _onSuccessfulRegistration.postValue(Unit)
            } else {
                _onUnSuccessfulRegistration.postValue(Unit)
            }
        }
    }
}