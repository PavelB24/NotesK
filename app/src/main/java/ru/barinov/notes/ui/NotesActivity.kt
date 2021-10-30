package ru.barinov.notes.ui

import Application
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.barinov.R
import ru.barinov.databinding.MainLayoutBinding
import ru.barinov.notes.domain.NotesRepository


class NotesActivity : AppCompatActivity() {
    private lateinit var binding: MainLayoutBinding
    private val fragmentManager: FragmentManager = supportFragmentManager
    private lateinit var bottomNavigationItemView: BottomNavigationView
    private val repository: NotesRepository
        get() = (application as Application).repository
    private val LOCAL_REPOSITORY_NAME = "local_repository.bin"

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = MainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavigation()
        bottomNavigationItemView.selectedItemId= R.id.notes_item_menu
    }

    private fun setNavigation() {
        //TODO
        bottomNavigationItemView = binding.navigationBar
        val savedData = Bundle()
        savedData.putParcelable(NotesRepository::class.java.canonicalName, repository)
        bottomNavigationItemView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.notes_item_menu -> {
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        fragmentManager.beginTransaction()
                            .replace(
                                R.id.container_for_fragment_land_1,
                                NoteListFragment.getInstance(savedData))
                            .commit()
                    } else {
                        fragmentManager.beginTransaction().replace(
                            R.id.container_for_fragment,
                            NoteListFragment.getInstance(savedData))
                            .commit()
                    }
                    fragmentManager.popBackStack()
                }
                R.id.data_manager_item_menu -> {
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        fragmentManager.beginTransaction()
                            .replace(R.id.container_for_fragment_land_1, DataManagerFragment())
                            .commit()
                    } else { fragmentManager.beginTransaction()
                        .replace(R.id.container_for_fragment, DataManagerFragment.getInstance(savedData))
                        .commit()
                    }
                }
            }; true
        }
    }


}