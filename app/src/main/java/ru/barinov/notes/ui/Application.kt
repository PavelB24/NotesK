package ru.barinov.notes.ui;

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.*
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import ru.barinov.notes.di.appModule
import ru.barinov.notes.domain.*



class Application : Application() {

    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@Application)
            modules(listOf(appModule))
        }
        super.onCreate()
    }
}

fun Context.application(): ru.barinov.notes.ui.Application {
    return applicationContext as ru.barinov.notes.ui.Application
}