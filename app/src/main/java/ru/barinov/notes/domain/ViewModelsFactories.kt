package ru.barinov.notes.domain

import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.ui.noteViewFragment.NoteViewViewModel

class ViewModelsFactories {

    class NoteViewViewModelFactory(
        val repository: NotesRepository,
        val id: String,
        val locationFinder: LocationFinder
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(
                NotesRepository::class.java,
                String::class.java,
                LocationFinder::class.java
            ).newInstance(repository, id, locationFinder)
        }
    }

    class NoteEditViewModelFactory(
        val title: String,
        val body: String,
        val datePicker: DatePicker,
        val id: String?,
        val repository: NotesRepository,
        val locationFinder: LocationFinder,
        val permission: Boolean
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(
                EditText::class.java, EditText::class.java, DatePicker::class.java, String::class.java,
                NotesRepository::class.java, LocationFinder::class.java, Boolean::class.java
            ).newInstance(
                title, body, datePicker,
                id, repository, locationFinder, permission
            )
        }

    }

}