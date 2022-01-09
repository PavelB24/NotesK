package ru.barinov.notes.ui.notesActivity

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

import ru.barinov.R
import ru.barinov.databinding.MainLayoutBinding
import ru.barinov.notes.domain.interfaces.Callable
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.Router
import ru.barinov.notes.domain.userRepository.NotesRepository
import ru.barinov.notes.ui.application

class ActivityMain : AppCompatActivity(), Callable {

    private lateinit var viewModel: ActivityViewModel
    private lateinit var binding: MainLayoutBinding
    lateinit var fabButton: FloatingActionButton
    lateinit var bottomAppBar: BottomAppBar
    private val fineLocation: String = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainLayoutBinding.inflate(layoutInflater)
        viewModel = ActivityViewModel(getRepository(), getCloudDB())
        setContentView(binding.root)
        askPermission()
        setBottomBar()
        downloadNotesFromFirebase()
    }

    private fun downloadNotesFromFirebase() {
        viewModel.readNotesFromCloud()
    }

    private fun askPermission() {
        this.requestPermissions(arrayOf(fineLocation), LOCATION_PERMISSION_REQUEST_CODE)

    }

    private fun setBottomBar() {
        fabButton = binding.addNoteFabButton!!
        bottomAppBar = binding.navigationBar as BottomAppBar
        bottomAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.notes_item_menu -> {
                    openNoteList()
                }
                R.id.data_manager_item_menu -> {
                    openDataManager()
                }
                R.id.profile_item_menu -> {
                    openProfile()
                }
            }; true
        }
        setStartFragment()
        fabButton.setOnClickListener { callEditionFragment(null) }
    }

    private fun setStartFragment() {
        viewModel.chooseStartFragment()
        viewModel.onChooseStartFragment.observe(this) {
            openStartFragment(it)
        }

    }

    private fun openProfile() {
        getRouter().openProfileFragment(
            getOrientation(), supportFragmentManager, getCloudDB().auth.currentUser != null
        )
    }

    private fun openStartFragment(fragment: Fragment) {
        getRouter().openOnStart(supportFragmentManager, fragment)
    }

    private fun openDataManager() {
        getRouter().openDataManagerFragment(getOrientation(), supportFragmentManager)
    }

    private fun openNoteList() {
        getRouter().openNoteLitFragment(getOrientation(), supportFragmentManager)
    }

    private fun getOrientation(): Int {
        return resources.configuration.orientation
    }

    override fun callEditionFragment(noteId: String?) {
        getRouter().openNoteEditFragment(noteId, supportFragmentManager)
    }

    override fun callNoteViewFragment(noteId: String) {
        getRouter().openNoteViewFragment(noteId, supportFragmentManager)
    }

    private fun getRepository(): NotesRepository {
        return application().repository
    }

    private fun getCloudDB(): CloudRepository {
        return application().cloudDataBase
    }

    private fun getRouter(): Router {
        return application().router
    }

}

