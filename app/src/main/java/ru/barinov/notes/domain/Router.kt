package ru.barinov.notes.domain

import android.content.res.Configuration
import androidx.fragment.app.FragmentManager
import ru.barinov.R
import ru.barinov.notes.ui.dataManagerFragment.DataManager
import ru.barinov.notes.ui.noteEditFragment.NoteEditFragment
import ru.barinov.notes.ui.noteListFragment.NoteList
import ru.barinov.notes.ui.noteViewFragment.NoteView
import ru.barinov.notes.ui.profileFragment.Profile
import ru.barinov.notes.ui.profileFragment.SignedFragment

class Router {
    var isLogged = false
    private var id: String? = null

    fun setId(id: String){
        this.id= id
    }

    fun getId(): String?{
        return id
    }

    fun resetId(){
        id= null
    }

    fun openNoteViewFragment(orientation:Int, fragmentManager: FragmentManager){
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager.popBackStackImmediate()
            fragmentManager.beginTransaction()
                .replace(R.id.container_for_fragment_land_2, NoteView()).addToBackStack(null)
                .commit()
        } else {
            fragmentManager.beginTransaction()
                .replace(R.id.container_for_fragment, NoteView()).addToBackStack(null)
                .commit()
        }
    }
    fun openNoteEditFragment(orientation:Int, fragmentManager: FragmentManager) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager.popBackStackImmediate()
            fragmentManager.beginTransaction()
                .replace(R.id.container_for_fragment_land_2, NoteEditFragment())
                .addToBackStack(null).commit()
        } else {
            fragmentManager.beginTransaction()
                .replace(R.id.container_for_fragment, NoteEditFragment())
                .addToBackStack(null).commit()
        }
    }
    fun openNoteLitFragment(orientation:Int, fragmentManager: FragmentManager) {
        fragmentManager.fragments.forEach { fragmentManager.beginTransaction().remove(it).commit() }
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager.beginTransaction()
                .add(R.id.container_for_fragment_land_1, NoteList())
                .commit()
        } else {
            fragmentManager.beginTransaction().replace(
                R.id.container_for_fragment,
                NoteList()
            )
                .commit()
        }
    }

    fun openDataManagerFragment(orientation:Int, fragmentManager: FragmentManager){
        fragmentManager.fragments.forEach { fragmentManager.beginTransaction().remove(it).commit() }
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager.beginTransaction()
                .replace(
                    R.id.layout_horizontal_unit_container,
                    DataManager()
                )
                .commit()
        } else {
            fragmentManager.beginTransaction()
                .replace(
                    R.id.container_for_fragment,
                    DataManager()
                )
                .commit()
        }
    }

    fun openProfileFragment(orientation:Int, fragmentManager: FragmentManager, isOnline: Boolean){
        fragmentManager.fragments.forEach { fragmentManager.beginTransaction().remove(it).commit() }
        if (orientation== Configuration.ORIENTATION_LANDSCAPE) {
            if(isOnline){
                fragmentManager.beginTransaction()
                    .replace(R.id.layout_horizontal_unit_container,
                        SignedFragment()).commit() }
            else{
            fragmentManager.beginTransaction()
                .replace(
                    R.id.layout_horizontal_unit_container,
                    Profile()
                ).commit()}
        } else {
            if(isOnline){
                fragmentManager.beginTransaction()
                    .replace(R.id.container_for_fragment,
                        SignedFragment()).commit() }
            else{
            fragmentManager.beginTransaction()
                .replace(R.id.container_for_fragment, Profile())
                .commit()}
        }
    }


}
