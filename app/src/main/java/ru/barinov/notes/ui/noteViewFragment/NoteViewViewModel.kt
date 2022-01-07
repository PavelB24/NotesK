package ru.barinov.notes.ui.noteViewFragment


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.barinov.notes.domain.LocationFinder
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity
import java.io.IOException


class NoteViewViewModel(
    val repository: NotesRepository,
    var id: String,
    private val locationFinder: LocationFinder
) : NoteViewContract.NoteViewFragmentPresenterInterface, ViewModel() {
    lateinit var tempNote: NoteEntity

    private val _openedNote = MutableLiveData<Array<String>>()
    val openedNote: LiveData<Array<String>> = _openedNote


    private val _latLong = MutableLiveData<Array<Double>>()
    val latLong: LiveData<Array<Double>> = _latLong





    override fun getNote() {
        var note = repository.getById(id)
        Log.d("@@@4", id)
        if (note != null) {
            tempNote = note
        } else {
            note = tempNote
        }

            Thread {
                var locationString = ""
                try{
                val address =
                    locationFinder.geocoder.getFromLocation(note.latitude, note.longitude, 1)
                        .firstOrNull()
               locationString = address?.countryName + ", " + address?.locality}
                catch (e: IOException){
                    //todo
                }

                _openedNote.postValue(
                    arrayOf(
                        note.title,
                        note.detail,
                        note.creationDate,
                        locationString
                    )
                )
                _latLong.postValue(arrayOf(note.latitude, note.longitude))
            }.start()
        }
    }
