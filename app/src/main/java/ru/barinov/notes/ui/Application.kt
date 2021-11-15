package ru.barinov.notes.ui;
import android.app.Application
import androidx.room.Room
import ru.barinov.notes.domain.*
import ru.barinov.notes.domain.Iterator


class Application : Application() {
    val repository = NotesRepository()
    val router= Router()
    val cache= Iterator()
    val  internet = Internet()
    lateinit var dataBase: DataBase


    override fun onCreate() {
       dataBase = Room.databaseBuilder(
            this,
            DataBase::class.java, "notes_database").allowMainThreadQueries().build()
        super.onCreate()
    }
}