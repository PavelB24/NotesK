package ru.barinov.notes.ui.notesActivity

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ru.barinov.R
import ru.barinov.databinding.MainLayoutBinding
import ru.barinov.notes.domain.Callable
import ru.barinov.notes.domain.NoteEntity
import ru.barinov.notes.domain.NotesRepository
import ru.barinov.notes.ui.*
import ru.barinov.notes.ui.dataManagerFragment.DataManagerFragment
import ru.barinov.notes.ui.noteEditFragment.NoteEditFragment
import ru.barinov.notes.ui.noteListFragment.NoteListFragment
import ru.barinov.notes.ui.noteViewFragment.NoteViewFragment
import java.io.*
import java.util.ArrayList


class NotesActivity : AppCompatActivity(), Callable {
    private var noteActivityPresenter = NoteActivityPresenter()
    private lateinit var binding: MainLayoutBinding
    private var fragmentManager: FragmentManager = supportFragmentManager
    private lateinit var bottomNavigationItemView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        noteActivityPresenter.onAttach(this)
        setNavigation()
        //todo переписать под 2 варианта хранения
        noteActivityPresenter.onReadNotes()
        bottomNavigationItemView.selectedItemId= R.id.notes_item_menu
    }

    private fun setNavigation() {
        //TODO
        bottomNavigationItemView = binding.navigationBar
        bottomNavigationItemView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.notes_item_menu -> {
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        fragmentManager.beginTransaction()
                            .replace(R.id.container_for_fragment_land_1, NoteListFragment())
                            .commit()
                    } else {
                        fragmentManager.beginTransaction().replace(
                            R.id.container_for_fragment,
                            NoteListFragment()
                        )
                            .commit()
                    }
                    fragmentManager.popBackStack()
                }
                R.id.data_manager_item_menu -> {
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        fragmentManager.beginTransaction()
                            .replace(R.id.container_for_fragment_land_1,
                                DataManagerFragment()
                            )
                            .commit()
                    } else {
                        fragmentManager.beginTransaction()
                            .replace(
                                R.id.container_for_fragment,
                                DataManagerFragment()
                            )
                            .commit()
                    }
                }
            }; true
        }
    }

    override fun onPause() {
        noteActivityPresenter.onSafeNotes()
        super.onPause()
    }

    override fun callEditionFragment() {
        fragmentManager.popBackStack()
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager.beginTransaction()
                .add(R.id.container_for_fragment_land_2, NoteEditFragment())
                .addToBackStack(null).commit()
        } else {
            fragmentManager.beginTransaction()
                .add(R.id.container_for_fragment, NoteEditFragment())
                .addToBackStack(null).commit()
        }
    }

    override fun callSettingsFragment() {
        TODO("Not yet implemented")
    }

    override fun callNoteViewFragment() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager.beginTransaction()
                .add(R.id.container_for_fragment_land_2, NoteViewFragment())
                .addToBackStack(null).commit()
        } else {
            fragmentManager.beginTransaction()
                .add(R.id.container_for_fragment, NoteViewFragment())
                .addToBackStack(null).commit()
        }
    }


}