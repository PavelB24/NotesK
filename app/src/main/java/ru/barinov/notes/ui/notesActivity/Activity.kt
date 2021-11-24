package ru.barinov.notes.ui.notesActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import ru.barinov.databinding.MainLayoutBinding
import ru.barinov.notes.domain.Callable
import ru.barinov.notes.ui.Application
import ru.barinov.notes.ui.application


class Activity : AppCompatActivity(), Callable {
    private var presenter = ActivityPresenter()
    private lateinit var binding: MainLayoutBinding
    lateinit var bottomNavigationItemView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter.onAttach(this)
        setNavigation()
        //todo переписать под 2 варианта хранения
        presenter.readNotes()
    }

    override fun onStart() {
        val currentUser = (application as Application).authentication.auth.currentUser
        if (currentUser != null) {
            //TODO
        }
        super.onStart()
    }

    private fun setNavigation() {
        bottomNavigationItemView = binding.navigationBar
        presenter.setNavigationListeners(bottomNavigationItemView)
    }


    override fun callEditionFragment() {
        presenter.editNote()
    }



    override fun callNoteViewFragment() {
     presenter.openNote()
    }

    override fun onDestroy() {
        application().repository.deleteAll()
        presenter.logOut()
        super.onDestroy()
    }




}
