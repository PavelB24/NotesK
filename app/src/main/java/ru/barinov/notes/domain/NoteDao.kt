package ru.barinov.notes.domain

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface NoteDao {

    @Insert
    fun addAll(notes: MutableList<NoteEntity>)

    @Delete
    fun delete(note: NoteEntity)

    @Query("SELECT * FROM notes_database")
    fun getAllNotes(): MutableList<NoteEntity>

    @Query("SELECT * FROM notes_database WHERE id == :id")
    fun getNoteById(id: String): NoteEntity

    @Query("DELETE FROM notes_database")
    fun clearDataBase()
}