package ru.barinov.notes.ui.noteEditFragment

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.location.Location
import android.location.LocationManager
import androidx.core.location.LocationListenerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.barinov.notes.domain.*
import ru.barinov.notes.domain.userRepository.NotesRepository
import ru.barinov.notes.domain.models.*
import ru.barinov.notes.ui.dataManagerFragment.DataManagerFragment
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("MissingPermission")
class NoteEditViewModel(
    private val tempNoteId: String?,
    private val repository: NotesRepository,
    private val locationFinder: LocationFinder,
    private val cloudRepository: CloudRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private var tempNote: NoteEntity? = null
    private lateinit var uuid: UUID
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private val _editionModeMessage = MutableLiveData<Unit>()
    val editionModeMessage: LiveData<Unit> = _editionModeMessage


    private val _fieldsIsNotFilledMassageLiveData = MutableLiveData<Unit>()
    val fieldsIsNotFilledMassageLiveData: LiveData<Unit> = _fieldsIsNotFilledMassageLiveData

    private val _viewContentLiveData = MutableLiveData<NoteDraft>()
    val viewContentLiveData: LiveData<NoteDraft> = _viewContentLiveData

    private val _closeScreen = MutableLiveData<Unit>()
    val closeScreenViewModel: LiveData<Unit> = _closeScreen

    private val locationListener = object : LocationListenerCompat {
        override fun onLocationChanged(location: Location) {
            latitude = location.latitude
            longitude = location.longitude
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }
    }

    fun startListenLocation() {
            locationFinder.locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000L, 1000F, locationListener
            )

    }

    //Переписать под получение строк и интов, а не вьюшек
    fun saveNote(draft: NoteDraft) {

        val title = draft.title
        val content = draft.content
        val type = draft.type
        val image = draft.image

        if (title.isEmpty() || content.isEmpty()) {
            _fieldsIsNotFilledMassageLiveData.postValue(Unit)
        } else {
            val note = createNote(title, content, type, image)
            repository.insertNote(note)

            if (sharedPreferences.getBoolean(DataManagerFragment.switchStateKey, false)) {
                Thread {
                    cloudRepository.writeInCloud(note)
                }.start()
            }
            removeLocationListener()
            _closeScreen.postValue(Unit)
        }
    }

    private fun createNote(title: String, content: String, type: NoteTypes, image: ByteArray): NoteEntity {
        return if (checkOnEditionMode()) {
           NoteEntity(
               id =tempNote!!.id,
               title = title,
               content = content,
               latitude = tempNote!!.latitude,
               longitude = tempNote!!.longitude,
               creationTime = tempNote!!.creationTime,
               isFavorite = tempNote!!.isFavorite,
               type= tempNote!!.type,
               image = tempNote!!.image
           )

        }//Создаём новую заметку
        else {
            uuid = UUID.randomUUID()
//            val dateFormat = SimpleDateFormat("dd/M/yyyy")
//            val currentTime = dateFormat.format(Date())
            val  currentTime = Date().time
            checkLatLong()
            NoteEntity(
                uuid.toString(),
                title = title,
                content = content,
                latitude = latitude,
                longitude = longitude,
                creationTime =currentTime ,
                isFavorite = false,
                type= type,
                image = image
            )
        }
    }

    private fun checkLatLong() {
        if (latitude == 0.0 || longitude == 0.0) {
            val lastKnownLocation =
                locationFinder.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            latitude = lastKnownLocation!!.latitude
            longitude = lastKnownLocation.longitude
        }
    }

    private fun checkOnEditionMode(): Boolean {
        if (tempNoteId != null) {
            tempNote = repository.getById(tempNoteId)
        }
        if (tempNote != null) {
            _editionModeMessage.postValue(Unit)
            return true
        }
        return false
    }

    fun fillTheViews() {
        if (checkOnEditionMode()) {
            _viewContentLiveData.postValue(NoteDraft(tempNote!!.title, tempNote!!.content, tempNote!!.type, tempNote!!.image))
        }
    }

    fun removeLocationListener() {
        locationFinder.locationManager.removeUpdates(locationListener)
    }

}


