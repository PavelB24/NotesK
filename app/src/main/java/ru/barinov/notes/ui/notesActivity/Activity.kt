package ru.barinov.notes.ui.notesActivity

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.google.android.material.bottomappbar.BottomAppBar

import ru.barinov.R
import ru.barinov.databinding.MainLayoutBinding
import ru.barinov.notes.domain.Authentication
import ru.barinov.notes.domain.Callable
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.Router
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.room.DataBase
import ru.barinov.notes.ui.application


class Activity : AppCompatActivity(), Callable {
    private lateinit var viewModel: ActivityViewModel
    private lateinit var binding: MainLayoutBinding
    lateinit var bottomAppBar: BottomAppBar
    private val fineLocation: String = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    //TODO переписать эдиттексты и текствью под матиреал, тулбар переписать под боттомтулбар с плавающей кнопкой

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainLayoutBinding.inflate(layoutInflater)
        viewModel =
            ActivityViewModel(getRepository(), getLocalDB(), getAuthentication(), getCloudDB())
        setContentView(binding.root)
        askPermission()
        setBottomBar()
        openLocalRepositoryNotes()
        downloadNotesFromFirebase()
        cloudSynchronizationListener()

    }



    private fun openLocalRepositoryNotes() {
        viewModel.readNotes()
        viewModel.onLocalBaseInitialised.observe(this){
            downloadNotesFromFirebase()
        }
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
            this.requestPermissions( arrayOf(fineLocation), LOCATION_PERMISSION_REQUEST_CODE)

    }





    private fun setBottomBar() {
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
    }

    private fun setStartFragment() {
        viewModel.chooseStartFragment()
        viewModel.onChooseStartFragment.observe(this) {
            openStartFragment(it)
        }


    }

    private fun openProfile() {
        getRouter().openProfileFragment(
            getOrientation(),
            supportFragmentManager,
            getAuthentication().auth.currentUser != null
        )
    }

    private fun openStartFragment(fragment: Fragment){
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


    override fun callEditionFragment() {
        getRouter().openNoteEditFragment(getOrientation(), supportFragmentManager)
    }


    override fun callNoteViewFragment() {
        getRouter().openNoteViewFragment(getOrientation(), supportFragmentManager)
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
