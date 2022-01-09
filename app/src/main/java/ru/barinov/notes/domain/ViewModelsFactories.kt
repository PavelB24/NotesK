package ru.barinov.notes.domain

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.barinov.notes.domain.adapters.NotesAdapter
import ru.barinov.notes.domain.userRepository.NotesRepository
import ru.barinov.notes.domain.interfaces.Callable

class ViewModelsFactories {

    class NoteViewViewModelFactory(
        val repository: NotesRepository,
        val id: String,
        val locationFinder: LocationFinder,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(NotesRepository::class.java,
                String::class.java,
                LocationFinder::class.java).newInstance(repository, id, locationFinder)
        }
    }

    class NoteListViewModelFactory(
        val repository: NotesRepository,
        val adapter: NotesAdapter,
        val cloudDataBase: CloudRepository,
        val activity: Callable,
        val sharedPref: SharedPreferences,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(NotesRepository::class.java,
                NotesAdapter::class.java,
                CloudRepository::class.java,
                Callable::class.java,
                SharedPreferences::class.java)
                .newInstance(repository, adapter, cloudDataBase, activity, sharedPref)
        }
    }

    class NoteEditViewModelFactory(
        val id: String?,
        val repository: NotesRepository,
        val locationFinder: LocationFinder,
        val permission: Boolean,
        val cloudRepository: CloudRepository,
        val sharedPreferences: SharedPreferences
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(String::class.java,
                NotesRepository::class.java,
                LocationFinder::class.java,
                Boolean::class.java,
                CloudRepository::class.java,
                SharedPreferences::class.java
            ).newInstance(id, repository, locationFinder, permission, cloudRepository, sharedPreferences)
        }

    }

}