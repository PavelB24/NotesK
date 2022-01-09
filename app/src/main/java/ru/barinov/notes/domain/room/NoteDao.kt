package ru.barinov.notes.domain.room

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.barinov.notes.domain.entity.NoteEntity

@Dao
interface NoteDao {

    @Insert
    fun addAll(notes: MutableList<NoteEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNote(note: NoteEntity)

    @Delete
    fun delete(note: NoteEntity)

    //todo delete
    @Query("SELECT * FROM note_table")
    fun getAllNotes(): MutableList<NoteEntity>

    //todo delete LD in name
    @Query("SELECT * FROM note_table")
    fun getAllNotesLiveData(): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM note_table WHERE id == :id")
    fun getNoteById(id: String): NoteEntity

    @Query("SELECT * FROM note_table WHERE id == :id")
    fun findNoteById(id: String): Boolean

    @Query("DELETE FROM note_table")
    fun clearDataBase()

    @Update
    fun update(note: NoteEntity)
}