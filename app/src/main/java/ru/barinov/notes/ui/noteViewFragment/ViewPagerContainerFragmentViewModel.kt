package ru.barinov.notes.ui.noteViewFragment

import androidx.lifecycle.*
import ru.barinov.notes.domain.models.NoteEntity
import ru.barinov.notes.domain.userRepository.NotesRepository

class ViewPagerContainerFragmentViewModel(private val repository: NotesRepository): ViewModel() {

    val noteListLiveData: LiveData<List<NoteEntity>> = repository.getNotesLiveData()


}