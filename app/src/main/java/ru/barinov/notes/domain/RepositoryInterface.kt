package ru.barinov.notes.domain

interface RepositoryInterface {
    val allNotes: MutableList<NoteEntity>
    val cache: ArrayList<NoteEntity>
    fun addNote(note: NoteEntity)
    fun addAll(list: MutableList<NoteEntity>)
    fun removeNote(id: String): Boolean
    fun updateNote(id: String, note: NoteEntity): Boolean
}