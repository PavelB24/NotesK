package ru.barinov.notes.ui;

import android.app.Application
import android.content.Context
import android.location.LocationManager
import androidx.room.Room
import com.google.firebase.FirebaseApp
import ru.barinov.notes.domain.*
import ru.barinov.notes.domain.userRepository.NotesRepository
import ru.barinov.notes.domain.room.DataBase


class Application : Application() {

    lateinit var repository: NotesRepository
    val router = Router()
    lateinit var localDataBase: DataBase
    lateinit var cloudDataBase: CloudRepository
    lateinit var locationFinder: LocationFinder

    override fun onCreate() {
        FirebaseApp.initializeApp(this)

        localDataBase = Room.databaseBuilder(
            this, DataBase::class.java, "notes_database"
        ).allowMainThreadQueries().build()

        cloudDataBase = CloudRepository()


        locationFinder = LocationFinder(
            getSystemService(LOCATION_SERVICE) as LocationManager, applicationContext
        )


        repository = NotesRepository(localDataBase.noteDao())

        super.onCreate()
    }
}

fun Context.application(): ru.barinov.notes.ui.Application {
    return applicationContext as ru.barinov.notes.ui.Application
}