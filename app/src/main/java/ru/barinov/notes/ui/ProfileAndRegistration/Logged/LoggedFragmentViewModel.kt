package ru.barinov.notes.ui.ProfileAndRegistration.Logged

import androidx.lifecycle.*
import ru.barinov.notes.domain.CloudRepository

class LoggedFragmentViewModel(private val cloud: CloudRepository) : ViewModel() {

    private val _onUnsuccessfulProfileExit = MutableLiveData<Unit>()
    val onUnsuccessfulProfileExit: LiveData<Unit> = _onUnsuccessfulProfileExit

    private val _userNameLiveData = MutableLiveData<String>()
    val userNameLiveData: LiveData<String> = _userNameLiveData

    fun onProfileOutButtonPressed() {
        singOut()
    }

    private fun singOut() {
        cloud.auth.signOut()
        _onUnsuccessfulProfileExit.postValue(Unit)
    }

    fun requireUserName() {
        val userName = cloud.auth.currentUser?.email.toString()
        _userNameLiveData.postValue(userName)
    }
}
