package ru.barinov.notes.di

import android.content.*
import android.location.*
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.*
import ru.barinov.notes.domain.*
import ru.barinov.notes.domain.room.*
import ru.barinov.notes.domain.userRepository.NotesRepository
import ru.barinov.notes.ui.ProfileAndRegistration.LoggedInFragment.LoggedFragmentViewModel
import ru.barinov.notes.ui.ProfileAndRegistration.ProfileEnteringFragment.*
import ru.barinov.notes.ui.ProfileAndRegistration.RegistrationFragment.RegistrationFragmentViewModel
import ru.barinov.notes.ui.dataManagerFragment.*
import ru.barinov.notes.ui.noteEditFragment.NoteEditViewModel
import ru.barinov.notes.ui.noteListFragment.NoteListViewModel
import ru.barinov.notes.ui.noteViewFragment.*
import ru.barinov.notes.ui.notesActivity.*

val appModule = module {

    single<DataBase> {
        Room.databaseBuilder(get(), DataBase::class.java, "notes_database").allowMainThreadQueries().build()
    }

    single<NoteDao> {
        get<DataBase>().noteDao() }

    single<NotesRepository> {
        NotesRepository(noteDao = get())
    }

    single<CloudRepository> {
        CloudRepository()
    }

    single<FirebaseAuth> {
        Firebase.auth
    }

    single<FirebaseFirestore> {
        Firebase.firestore
    }

    single<LocationFinder> {
        LocationFinder(
            get(), get()
        )
    }

    single<LocationManager> {
        androidApplication().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    single<Geocoder> {
        Geocoder(get())
    }


    single  <SharedPreferences> {
        androidApplication().getSharedPreferences(DataManagerFragment.sharedPreferencesName, Context.MODE_PRIVATE)
    }




    viewModel<ActivityViewModel> {
        ActivityViewModel(
            repository = get(), cloudDataBase = get()
        )
    }

    viewModel<DataManagerViewModel> {
        DataManagerViewModel(get(), get(), get())
    }

    viewModel<NoteEditViewModel> {noteId->
        NoteEditViewModel(noteId.get(), get(), get(), get(), get() )
    }

    viewModel<NoteListViewModel>{
        NoteListViewModel(get(), get(), get())
    }

    viewModel<LoggedFragmentViewModel> {
        LoggedFragmentViewModel(get())
    }

    viewModel<ProfileEnteringFragmentViewModel> {
        ProfileEnteringFragmentViewModel(get(),get(), get())
    }

    viewModel<RegistrationFragmentViewModel> {
        RegistrationFragmentViewModel(get())
    }

    viewModel<NotePageViewModel>{parameterId->
        NotePageViewModel(parameterId.get(), get(), get())
    }




}
