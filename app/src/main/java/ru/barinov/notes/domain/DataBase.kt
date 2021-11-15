package ru.barinov.notes.domain

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NoteEntity::class], version = 1)
abstract class DataBase: RoomDatabase() {
     abstract fun noteDao(): NoteDao


}