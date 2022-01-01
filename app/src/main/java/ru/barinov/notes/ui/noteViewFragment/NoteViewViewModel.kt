package ru.barinov.notes.ui.noteViewFragment


import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.barinov.notes.domain.LocationFinder
import ru.barinov.notes.domain.Router
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity
import ru.barinov.notes.ui.dialogs.MapsFragment
import ru.barinov.notes.ui.notesActivity.Activity


class NoteViewViewModel(
    val repository: NotesRepository,
    val router: Router,
    private val locationFinder: LocationFinder
) : NoteViewContract.NoteViewFragmentPresenterInterface, ViewModel() {
    lateinit var tempNote: NoteEntity

    private val _openedNote = MutableLiveData<Array<String>>()
    val openedNote: LiveData<Array<String>> = _openedNote


    private val _latLong = MutableLiveData<Array<Double>>()
    val latLong: LiveData<Array<Double>> = _latLong




    override fun getNote() {
        var note = repository.getById(router.getId())
        Log.d("@@@", note.toString())
        if(note!=null){
            tempNote=note
        }else {
            note= tempNote
        }
        Thread{
        val address = locationFinder.geocoder.getFromLocation(note.latitude, note.longitude, 1).firstOrNull()
            val locationString= address?.countryName + ", " + address?.locality
              _openedNote.postValue(arrayOf(note.title, note.detail, note.creationDate, locationString))
            _latLong.postValue(arrayOf(note.latitude, note.longitude))
            resetIdInRouter()
        }.start()
    }



    private fun resetIdInRouter(){
        router.resetId()
    }


}