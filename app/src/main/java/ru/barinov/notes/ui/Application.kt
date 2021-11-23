package ru.barinov.notes.ui;

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.firebase.FirebaseApp
import ru.barinov.notes.domain.*
import ru.barinov.notes.domain.curentDataBase.Iterator
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.room.DataBase


class Application : Application() {
    val repository = NotesRepository()
    val router = Router()
    val cache = Iterator()
    lateinit var authentication: Authentication
    lateinit var localDataBase: DataBase
    lateinit var cloudDataBase: CloudRepository



    override fun onCreate() {
        FirebaseApp.initializeApp(this);
        cloudDataBase = CloudRepository()
        authentication = Authentication()
        localDataBase = Room.databaseBuilder(
            this,
            DataBase::class.java, "notes_database"
        ).allowMainThreadQueries().build()
        super.onCreate()
    }
}

fun Context.application(): ru.barinov.notes.ui.Application {
    return applicationContext as ru.barinov.notes.ui.Application
}