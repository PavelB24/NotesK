package ru.barinov.notes.ui;
import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import ru.barinov.notes.domain.*
import ru.barinov.notes.domain.Iterator


class Application : Application() {
    val repository = NotesRepository()
    val router= Router()
    val cache= Iterator()
    val  internet = Internet()
    lateinit var localDataBase: DataBase


    override fun onCreate() {
        val fDataBase = FirebaseFirestore.getInstance()
       localDataBase = Room.databaseBuilder(
            this,
            DataBase::class.java, "notes_database").allowMainThreadQueries().build()
        super.onCreate()
    }
}

fun Context.application(): ru.barinov.notes.ui.Application{
    return applicationContext as ru.barinov.notes.ui.Application
}