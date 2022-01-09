package ru.barinov.notes.ui.noteViewFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.barinov.notes.domain.LocationFinder
import ru.barinov.notes.domain.userRepository.NotesRepository
import java.io.IOException

class NotePageViewModel(
    private val repository: NotesRepository, private val id: String, private val locationFinder: LocationFinder
) : ViewModel() {


    private val _openedNotesDraft = MutableLiveData<NoteDraftExtended>()
    val openedNoteDraft: LiveData<NoteDraftExtended> = _openedNotesDraft



    fun getNote() {
        val note = repository.getById(id)
        Log.d("@@@4", id)
        Thread {
            var locationString = ""
            try {
                val address =
                    locationFinder.geocoder.getFromLocation(note.latitude, note.longitude, 1).firstOrNull()
                locationString = address?.countryName + ", " + address?.locality
            } catch (e: IOException) {
                //todo
            }
            _openedNotesDraft.postValue(NoteDraftExtended(note.title, note.content, note.creationDate, locationString, note.latitude, note.longitude))
        }.start()
    }
}
