package ru.barinov.notes.ui.notesActivity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener

import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.barinov.R
import ru.barinov.databinding.MainLayoutBinding
import ru.barinov.notes.domain.Authentication
import ru.barinov.notes.domain.Callable
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.Router
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.room.DataBase
import ru.barinov.notes.ui.Application
import ru.barinov.notes.ui.application
import ru.barinov.notes.ui.dataManagerFragment.DataManager
import java.util.jar.Manifest


class Activity : AppCompatActivity(), Callable {
    private lateinit var viewModel: ActivityViewModel
    private lateinit var binding: MainLayoutBinding
    lateinit var bottomNavigationItemView: BottomNavigationView
    private val coarseLocation: String = android.Manifest.permission.ACCESS_COARSE_LOCATION
    private val fineLocation: String = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainLayoutBinding.inflate(layoutInflater)
        viewModel =
            ActivityViewModel(getRepository(), getLocalDB(), getAuthentication(), getCloudDB())
        setContentView(binding.root)
        downloadNotesFromFirebase()
        cloudSynchronizationListener()
        askPermission()
        setNavigation()

    }

    private fun cloudSynchronizationListener() {
        viewModel.onCloudInitCompleted.observe(this){
            supportFragmentManager.setFragmentResult(this.javaClass.simpleName, Bundle().also { it.putBoolean(this.javaClass.simpleName, true) })
        }
    }


    private fun downloadNotesFromFirebase() {
            Log.d("@@@", "LISTENER2: ")
            viewModel.readNotesFromCloud()
    }

    private fun askPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, coarseLocation) ||
            ActivityCompat.shouldShowRequestPermissionRationale(this, fineLocation)){
            ActivityCompat.requestPermissions(this, arrayOf(coarseLocation, fineLocation), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }



    private fun setNavigation() {
        bottomNavigationItemView = binding.navigationBar
        bottomNavigationItemView.setOnItemSelectedListener { item ->
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
    }

    private fun setStartFragment() {
        viewModel.chooseStartFragment()
        viewModel.onChooseStartFragment.observe(this) {
            bottomNavigationItemView.selectedItemId = it
        }


    }

    private fun openProfile() {
        getRouter().openProfileFragment(
            getOrientation(),
            supportFragmentManager,
            getAuthentication().auth.currentUser != null
        )
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


    override fun callEditionFragment() {
        getRouter().openNoteEditFragment(getOrientation(), supportFragmentManager)
    }


    override fun callNoteViewFragment() {
        getRouter().openNoteViewFragment(getOrientation(), supportFragmentManager)
    }

    override fun onDestroy() {
        application().repository.deleteAll()
        viewModel.logOut()
        super.onDestroy()
    }

    private fun getRepository(): NotesRepository {
        return application().repository
    }

    private fun getLocalDB(): DataBase {
        return application().localDataBase
    }

    private fun getCloudDB(): CloudRepository {
        return application().cloudDataBase
    }

    private fun getAuthentication(): Authentication {
        return application().authentication
    }

    private fun getRouter(): Router {
        return application().router
    }


}
