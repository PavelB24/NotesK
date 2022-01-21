package ru.barinov.notes.ui.noteViewFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.barinov.notes.domain.LocationFinder
import ru.barinov.notes.domain.models.NoteEntity
import ru.barinov.notes.domain.userRepository.NotesRepository
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class NotePageViewModel(
    private val id: String,
    private val repository: NotesRepository,
    private val locationFinder: LocationFinder
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

            _openedNotesDraft.postValue(
                NoteDraftExtended(
                    note.title,
                    note.content,
                    convertTimeInFormattedString(note),
                    locationString,
                    note.latitude,
                    note.longitude,
                    note.type,
                    note.image
                )
            )
        }.start()
    }

    private fun convertTimeInFormattedString(note: NoteEntity): String {
        val dateFormat = SimpleDateFormat("dd/M/yyyy")
        val date = Date()
        date.time = note.creationTime
        return dateFormat.format(date)

    }
}
