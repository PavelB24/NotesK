package ru.barinov.notes.ui

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.barinov.R
import ru.barinov.databinding.MainLayoutBinding
import ru.barinov.notes.domain.Callable
import ru.barinov.notes.domain.NotesRepository


class NotesActivity : AppCompatActivity(), Callable {
    private lateinit var binding: MainLayoutBinding
    private var fragmentManager: FragmentManager = supportFragmentManager
    private lateinit var bottomNavigationItemView: BottomNavigationView
    private val repository: NotesRepository
        get() = (application as Application).repository
    private val LOCAL_REPOSITORY_NAME = "local_repository.bin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("@@@","ёба")
        binding = MainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavigation()
        bottomNavigationItemView.selectedItemId= R.id.notes_item_menu
    }

    private fun setNavigation() {
        //TODO
        bottomNavigationItemView = binding.navigationBar
        val savedData = Bundle()
        savedData.putParcelable(NotesRepository::class.simpleName, repository)
        if (!savedData.isEmpty){ Log.d("@@@","не пусто")}
        bottomNavigationItemView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.notes_item_menu -> {
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        fragmentManager.beginTransaction()
                            .replace(
                                R.id.container_for_fragment_land_1,
                                NoteListFragment.getInstance(savedData)
                            )
                            .commit()
                    } else {
                        fragmentManager.beginTransaction().replace(
                            R.id.container_for_fragment,
                            NoteListFragment.getInstance(savedData)
                        )
                            .commit()
                    }
                    fragmentManager.popBackStack()
                }
                R.id.data_manager_item_menu -> {
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        fragmentManager.beginTransaction()
                            .replace(R.id.container_for_fragment_land_1, DataManagerFragment.getInstance(savedData))
                            .commit()
                    } else {
                        fragmentManager.beginTransaction()
                            .replace(
                                R.id.container_for_fragment,
                                DataManagerFragment.getInstance(savedData)
                            )
                            .commit()
                    }
                }
            }; true
        }
    }

    override fun callEditionFragment(data: Bundle?) {
        fragmentManager.popBackStack()
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager.beginTransaction()
                .add(R.id.container_for_fragment_land_2, NoteEditFragment.getInstance(data))
                .addToBackStack(null).commit()
        } else {
            fragmentManager.beginTransaction()
                .add(R.id.container_for_fragment, NoteEditFragment.getInstance(data))
                .addToBackStack(null).commit()
        }
    }

    override fun callSettingsFragment() {
        TODO("Not yet implemented")
    }

    override fun callNoteViewFragment(data: Bundle?) {
//        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            fragmentManager.beginTransaction()
//                .add(R.id.container_for_fragment_land_2, NoteViewFragment.getInstance(data))
//                .addToBackStack(null).commit()
//        } else {
//            fragmentManager.beginTransaction()
//                .add(R.id.container_for_fragment, NoteViewFragment.getInstance(data))
//                .addToBackStack(null).commit()
//        }
    }


}