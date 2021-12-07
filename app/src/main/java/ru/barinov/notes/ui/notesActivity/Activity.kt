package ru.barinov.notes.ui.notesActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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


class Activity : AppCompatActivity(), Callable {
    private var presenter = ActivityViewModel(getRepository(), getLocalDB(), getAuthentication(), getCloudDB())
    private lateinit var binding: MainLayoutBinding
    lateinit var bottomNavigationItemView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavigation()
        //todo переписать под 2 варианта хранения
         var res: Bundle? = null
        supportFragmentManager.setFragmentResultListener(
            DataManager::class.simpleName!!,
            this,
            FragmentResultListener { requestKey, result ->
                   res= result

            })
        presenter.readNotes()
        if(res != null){
        presenter.readNotesFromCloud(res)}
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
        presenter.chooseStartFragment()
        presenter.onChooseStartFragment.observe(this){
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
        presenter.logOut()
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
