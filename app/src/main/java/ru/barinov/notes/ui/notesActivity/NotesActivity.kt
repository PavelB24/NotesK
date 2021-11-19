package ru.barinov.notes.ui.notesActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.barinov.databinding.MainLayoutBinding
import ru.barinov.notes.domain.Callable


class NotesActivity : AppCompatActivity(), Callable {
    private var presenter = NoteActivityPresenter()
    private lateinit var binding: MainLayoutBinding
    lateinit var bottomNavigationItemView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter.onAttach(this)
        setNavigation()
        //todo переписать под 2 варианта хранения
        presenter.onReadNotes()
    }

    private fun setNavigation() {
        //TODO
        bottomNavigationItemView = binding.navigationBar
        presenter.setNavigationListeners(bottomNavigationItemView)
    }

    override fun onPause() {
        presenter.onSafeNotes()
        super.onPause()
    }

    override fun callEditionFragment() {
        presenter.editNote()
    }

    override fun callSettingsFragment() {
        TODO("Not yet implemented")
    }

    override fun callNoteViewFragment() {
     presenter.openNote()
    }

    fun getBinding(): MainLayoutBinding{
        return binding
    }


}
