package ru.barinov.notes.ui.noteEditFragment


import android.annotation.SuppressLint
import android.location.Location

import android.location.LocationListener
import android.location.LocationManager

import android.os.Bundle
import android.util.Log
import android.widget.CheckBox

import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.core.location.LocationListenerCompat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.barinov.notes.domain.LocationFinder
import ru.barinov.notes.domain.Router
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("MissingPermission")
class NoteEditViewModel(
    private val title: EditText,
    private val body: EditText,
    val router: Router,
    repository: NotesRepository,
    private val locationFinder: LocationFinder,
    private val permission: Boolean,
) : ViewModel(), NoteEditContract.NoteEditFragmentPresenterInterface {
    private var tempNote: NoteEntity? = repository.getById(router.getId())
    private lateinit var uuid: UUID
    private var data: Bundle? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0


    private val _fieldsIsNotFilledMassageLiveData = MutableLiveData<Unit>()
    val fieldsIsNotFilledMassageLiveData: LiveData<Unit> = _fieldsIsNotFilledMassageLiveData

    private val _viewContentLiveData = MutableLiveData<Array<String>>()
    val viewContentLiveData: LiveData<Array<String>> = _viewContentLiveData

    private val _dataForFragmentResult = MutableLiveData<Bundle>()
    val dataForFragmentResult: LiveData<Bundle> = _dataForFragmentResult



    private val locationListener = object: LocationListenerCompat{
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
        if (permission) {
            locationFinder.locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000L,
                1000F,
                locationListener
            )
        }
    }


    //Переписать под получение строк и интов, а не вьюшек
    override fun initSafeNote() {
        uuid = UUID.randomUUID()
        val dateFormat = SimpleDateFormat("dd/M/yyyy")
        val currentDate = dateFormat.format(Date())


        //Редактирование
        if (checkOnEditionMode() && (title.text.toString().isNotEmpty() && body.text.toString()
                .isNotEmpty())
        ) {
            val note = NoteEntity(
                tempNote!!.id,
                title.text.toString(),
                body.text.toString(),
                tempNote!!.latitude,
                tempNote!!.longitude,
                tempNote!!.creationDate,
                tempNote!!.isFavorite
            )
            data = Bundle()
            data?.putParcelable(NoteEntity::class.simpleName, note)
            _dataForFragmentResult.postValue(data!!)

        }//Создаём новую заметку
        else if (title.text.toString().isNotEmpty() && body.text.toString().isNotEmpty()) {
            checkLatLong()
            val note = NoteEntity(
                uuid.toString(), title.text.toString(),
                body.text.toString(),
                latitude, longitude,
                currentDate, false
            )
            data = Bundle()
            data?.putParcelable(NoteEntity::class.simpleName, note)
            _dataForFragmentResult.postValue(data!!)
        } else {
            _fieldsIsNotFilledMassageLiveData.postValue(Unit)
        }
        Log.d("@@@2", "$longitude $latitude")
        removeLocationListener()
    }



    private fun checkLatLong() {
        if (latitude == 0.0 || longitude == 0.0) {
            val lastKnownLocation= locationFinder.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            latitude= lastKnownLocation!!.latitude
            longitude= lastKnownLocation.longitude
        }
    }


    override fun checkOnEditionMode(): Boolean {
        if (!(tempNote == null)) {
            return true
        }
        return false
    }


    fun fillTheViews() {
        if (checkOnEditionMode()) {
            _viewContentLiveData.postValue(
                arrayOf(
                    tempNote!!.title,
                    tempNote!!.detail,
                )
            )
        }
    }

    fun removeLocationListener() {
        locationFinder.locationManager.removeUpdates(locationListener)
    }

    fun clearRouterId() {
        router.resetId()
    }


}


