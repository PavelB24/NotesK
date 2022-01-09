package ru.barinov.notes.domain.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.barinov.notes.domain.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 1)
abstract class DataBase: RoomDatabase() {
     abstract fun noteDao(): NoteDao
}