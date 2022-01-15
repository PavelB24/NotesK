package ru.barinov.notes.ui.notesActivity

import android.content.res.Configuration
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.barinov.R
import ru.barinov.notes.ui.dataManagerFragment.DataManagerFragment
import ru.barinov.notes.ui.noteEditFragment.NoteEditFragment
import ru.barinov.notes.ui.noteListFragment.NoteListFragment
import ru.barinov.notes.ui.noteViewFragment.ViewPagerContainerFragment
import ru.barinov.notes.ui.ProfileAndRegistration.ProfileEntering.ProfileEnteringFragment
import ru.barinov.notes.ui.ProfileAndRegistration.Logged.LoggedFragment

class Router {

    fun openOnStart(fragmentManager: FragmentManager, fragment: Fragment) {

        fragmentManager.beginTransaction().replace(R.id.container_for_fragment, fragment).commit()
    }

    fun openNoteViewFragment(id: String, fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction()
            .replace(R.id.container_for_fragment, ViewPagerContainerFragment.getInstance(id)).addToBackStack(null).commit()
    }

    fun openNoteEditFragment(id: String?, fragmentManager: FragmentManager) {

        fragmentManager.beginTransaction().add(R.id.container_for_fragment, NoteEditFragment.getInstance(id))
            .addToBackStack(null).commit()
    }

    fun openNoteLitFragment( fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction().replace(
                R.id.container_for_fragment, NoteListFragment()
            ).commit()
    }

    fun openDataManagerFragment(orientation: Int, fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction().replace(
            R.id.container_for_fragment, DataManagerFragment()
        ).commit()
    }

    fun openProfileFragment(fragmentManager: FragmentManager, isOnline: Boolean) {
        if (isOnline) {
            fragmentManager.beginTransaction().replace(
                R.id.container_for_fragment, LoggedFragment()
            ).commit()
        } else {
            fragmentManager.beginTransaction().replace(R.id.container_for_fragment, ProfileEnteringFragment())
                .commit()
        }
    }
}



