package ru.barinov.notes.domain

import androidx.room.*


@Dao
interface NoteDao {

    @Insert
    fun addAll(notes: MutableList<NoteEntity>)

    @Insert
    fun addNote(note: NoteEntity)

    @Delete
    fun delete(note: NoteEntity)

    @Query("SELECT * FROM notes_database")
    fun getAllNotes(): MutableList<NoteEntity>

    @Query("SELECT * FROM notes_database WHERE id == :id")
    fun getNoteById(id: String): NoteEntity

    @Query("DELETE FROM notes_database")
    fun clearDataBase()

    @Update
    fun update(note: NoteEntity)
}