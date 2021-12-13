package ru.barinov.notes.ui.noteEditFragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationListenerCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.barinov.notes.domain.LocationFinder
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity
import java.util.*


@SuppressLint("MissingPermission")
class NoteEditViewModel(
    private val title: EditText,
    private val body: EditText,
    private val datePicker: DatePicker,
    id: String?,
    repository: NotesRepository,
    private val locationFinder: LocationFinder,
    private val permission: Boolean
) : NoteEditContract.NoteEditFragmentPresenterInterface {
    private var tempNote: NoteEntity? = repository.getById(id)
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

    val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            latitude = location.latitude
            longitude = location.longitude
        }
    }


    fun startListenLocation() {
        if (permission) {
            locationFinder.locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000L,
                1000f,
                locationListener
            )
        }
    }


    //Переписать под получение строк и интов, а не вьюшек
    override fun initSafeNote() {
        //todo вынести кнопку во вьюху + передавать сюда не вьюшки, а строки
        uuid = UUID.randomUUID()
        //Редактирование
        if (checkOnEditionMode() && (title.text.toString().isNotEmpty() && body.text.toString()
                .isNotEmpty())
        ) {
            val note = NoteEntity(
                tempNote!!.id,
                title.text.toString(),
                body.text.toString(),
                datePicker.dayOfMonth,
                datePicker.month,
                datePicker.year,
                tempNote!!.latitude,
                tempNote!!.longitude
            )
            data = Bundle()
            data?.putParcelable(NoteEntity::class.simpleName, note)
            _dataForFragmentResult.postValue(data!!)

        }//Создаём новую заметку
        else if (title.text.toString().isNotEmpty() && body.text.toString().isNotEmpty()) {
            val note = NoteEntity(
                uuid.toString(), title.text.toString(),
                body.text.toString(),
                datePicker.dayOfMonth, datePicker.month, datePicker.year, latitude, longitude
            )
            data = Bundle()
            data?.putParcelable(NoteEntity::class.simpleName, note)
            _dataForFragmentResult.postValue(data!!)
        } else {
            _fieldsIsNotFilledMassageLiveData.postValue(Unit)
        }
        locationFinder.locationManager.removeUpdates(locationListener)
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
                    tempNote!!.originYear.toString(),
                    tempNote!!.originMonth.toString(),
                    tempNote!!.originDay.toString()
                )
            )
        }
    }


}


