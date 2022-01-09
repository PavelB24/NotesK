package ru.barinov.notes.domain.userRepository

import androidx.lifecycle.LiveData
import ru.barinov.notes.domain.entity.NoteEntity
import ru.barinov.notes.domain.room.NoteDao

class NotesRepository(private val noteDao: NoteDao) {

    //todo rename without Room in name
    fun insertNote(note: NoteEntity) {
        noteDao.addNote(note)
    }

    fun removeAllNotes() {
        noteDao.clearDataBase()
    }

    fun removeNote(note: NoteEntity) {
        noteDao.delete(note)
    }

    fun findById(id: String): Boolean {
        return noteDao.findNoteById(id)
    }

    fun getById(id: String): NoteEntity {
        return noteDao.getNoteById(id)
    }

    fun getNotesLiveData(): LiveData<List<NoteEntity>> {
        return noteDao.getAllNotesLiveData()
    }

}