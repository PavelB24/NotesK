package ru.barinov.notes.domain

import android.content.res.Configuration
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.barinov.R
import ru.barinov.notes.ui.dataManagerFragment.DataManagerFragment
import ru.barinov.notes.ui.noteEditFragment.NoteEditFragment
import ru.barinov.notes.ui.noteListFragment.NoteListFragment
import ru.barinov.notes.ui.noteViewFragment.ViewPagerContainerFragment
import ru.barinov.notes.ui.profileFragment.Profile
import ru.barinov.notes.ui.profileFragment.LoggedFragment

//todo передавать айди как аргумент
class Router {

    fun openOnStart(fragmentManager: FragmentManager, fragment: Fragment) {

        fragmentManager.beginTransaction().replace(R.id.container_for_fragment, fragment).commit()
    }

    fun openNoteViewFragment(id: String, fragmentManager: FragmentManager) {

        fragmentManager
            .beginTransaction()
            .replace(R.id.container_for_fragment, ViewPagerContainerFragment.getInstance(id))
            .commit()
    }

    fun openNoteEditFragment(id: String?, fragmentManager: FragmentManager) {

        fragmentManager.beginTransaction().add(R.id.container_for_fragment, NoteEditFragment.getInstance(id))
            .addToBackStack(null).commit()
    }

    fun openNoteLitFragment(orientation: Int, fragmentManager: FragmentManager) {
        fragmentManager.fragments.forEach { fragmentManager.beginTransaction().remove(it).commit() }
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager.beginTransaction().add(R.id.container_for_fragment_land_1, NoteListFragment())
                .commit()
        } else {
            fragmentManager
                .beginTransaction()
                .replace(
                    R.id.container_for_fragment, NoteListFragment()
                ).commit()
        }
    }

    fun openDataManagerFragment(orientation: Int, fragmentManager: FragmentManager) {
        fragmentManager.fragments.forEach { fragmentManager.beginTransaction().remove(it).commit() }
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager.beginTransaction().replace(
                R.id.layout_horizontal_unit_container, DataManagerFragment()
            ).commit()
        } else {
            fragmentManager.beginTransaction().replace(
                R.id.container_for_fragment, DataManagerFragment()
            ).commit()
        }
    }

    fun openProfileFragment(orientation: Int, fragmentManager: FragmentManager, isOnline: Boolean) {
        fragmentManager.fragments.forEach { fragmentManager.beginTransaction().remove(it).commit() }
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (isOnline) {
                fragmentManager.beginTransaction().replace(
                    R.id.layout_horizontal_unit_container, LoggedFragment()
                ).commit()
            } else {
                fragmentManager.beginTransaction().replace(
                    R.id.layout_horizontal_unit_container, Profile()
                ).commit()
            }
        } else {
            if (isOnline) {
                fragmentManager.beginTransaction().replace(
                    R.id.container_for_fragment, LoggedFragment()
                ).commit()
            } else {
                fragmentManager.beginTransaction().replace(R.id.container_for_fragment, Profile()).commit()
            }
        }
    }
}


