package ru.barinov.notes.ui;
import android.app.Application
import ru.barinov.notes.domain.Iterator
import ru.barinov.notes.domain.NotesRepository
import ru.barinov.notes.domain.Router

class Application : Application() {
    val repository = NotesRepository()
    val router= Router()
    val cache= Iterator()
}