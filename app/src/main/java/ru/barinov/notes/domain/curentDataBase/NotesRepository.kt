package ru.barinov.notes.domain.curentDataBase

import android.util.Log
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity

class NotesRepository {

    val allNotes: MutableList<NoteEntity> = mutableListOf()

    fun deleteAll() {
        allNotes.clear()
    }

    fun addNote(note: NoteEntity) {
        Log.d("@@@", note.toString() + 1)
        allNotes.add(note)
        Log.d("@@@", allNotes.toString() + 2)
    }

    fun addAll(list: MutableList<NoteEntity>) {
        allNotes.addAll(list)
    }

    fun removeNote(id: String): Boolean {
        val range = 0 until allNotes.size
        for (i in range) {
            if (allNotes[i].id.equals(id)) {
                allNotes.removeAt(i)
                return true
            }
        }
        return false
    }

    fun updateNote(id: String, note: NoteEntity): Boolean {
        removeNote(id)
        note.id = id
        this.addNote(note)
        return true
    }


    fun findById(id: String): Boolean {
        for (note in allNotes) {
            if (note.id.equals(id)) {
                return true
            }
        }
        return false
    }

    fun getById(id: String?): NoteEntity? {
        for (note in allNotes) {
            if (note.id.equals(id)) {
                return note
            }
        }
        return null
    }

    fun getNotes(): MutableList<NoteEntity> {
        return ArrayList<NoteEntity>(allNotes)
    }
}