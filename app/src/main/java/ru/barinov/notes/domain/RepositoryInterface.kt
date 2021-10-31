package ru.barinov.notes.domain

interface RepositoryInterface {
    val allNotes: MutableList<NoteEntity>
    val searchCache: ArrayList<NoteEntity>
    fun addNote(note: NoteEntity)
    fun addAll(arrayList: List<NoteEntity>)
    fun removeNote(id: String): Boolean
    fun updateNote(id: String, note: NoteEntity): Boolean
}