package ru.barinov.notes.domain

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity
import ru.barinov.notes.ui.noteViewFragment.NoteView
import ru.barinov.notes.ui.noteViewFragment.ViewPagerContainerFragment

class NoteViewPagerAdapter( fragment: Fragment, val router: Router): FragmentStateAdapter(fragment) {
     lateinit var noteList: List<NoteEntity>

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun createFragment(position: Int): Fragment {
        val note = noteList[position]
        router.setId(note.id)
        Log.d("@@@3", "createFragment: " + note.id)
        return NoteView()

    }
}