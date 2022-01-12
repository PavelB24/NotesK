package ru.barinov.notes.ui.notesActivity

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.R
import ru.barinov.databinding.MainLayoutBinding
import ru.barinov.notes.domain.interfaces.ActivityCallableInterface
import ru.barinov.notes.domain.Router

class ActivityMain : AppCompatActivity(), ActivityCallableInterface {

    private val viewModel by viewModel<ActivityViewModel>()
    private lateinit var binding: MainLayoutBinding
    lateinit var fabButton: FloatingActionButton
    lateinit var bottomNavigationView: BottomNavigationView
    private val fineLocation: String = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val router = Router()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainLayoutBinding.inflate(layoutInflater)
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
        fabButton = binding.addNoteFabButton
        bottomNavigationView = binding.navigationBar as BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener { item ->
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
        viewModel.onChooseStartFragment.observe(this) {id->
            bottomNavigationView.selectedItemId=id
        }

    }

    private fun openProfile() {
        viewModel.checkOnUserOnline()
        viewModel.hasLoggedUser.observe(this){ hasCurrentUser->
            router.openProfileFragment(supportFragmentManager, hasCurrentUser)
        }


    }

    private fun openStartFragment(fragment: Fragment) {
        router.openOnStart(supportFragmentManager, fragment)
    }

    private fun openDataManager() {
        router.openDataManagerFragment(getOrientation(), supportFragmentManager)
    }

    private fun openNoteList() {
        router.openNoteLitFragment( supportFragmentManager)
    }

    private fun getOrientation(): Int {
        return resources.configuration.orientation
    }

    override fun callEditionFragment(noteId: String?) {
        router.openNoteEditFragment(noteId, supportFragmentManager)
    }

    override fun callNoteViewFragment(noteId: String) {
        router.openNoteViewFragment(noteId, supportFragmentManager)
    }


}

