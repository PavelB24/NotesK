package ru.barinov.notes.domain.curentDataBase

import ru.barinov.notes.domain.noteEntityAndService.NoteEntity

interface RepositoryInterface {
    val allNotes: MutableList<NoteEntity>
    fun addNote(note: NoteEntity)
    fun addAll(list: MutableList<NoteEntity>)
    fun removeNote(id: String): Boolean
    fun updateNote(id: String, note: NoteEntity): Boolean
}