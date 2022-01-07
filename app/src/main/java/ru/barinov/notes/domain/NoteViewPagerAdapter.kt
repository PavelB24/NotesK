package ru.barinov.notes.domain

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity
import ru.barinov.notes.ui.noteViewFragment.NoteViewFragment

class NoteViewPagerAdapter( fragment: Fragment): FragmentStateAdapter(fragment) {
     lateinit var noteList: MutableList<NoteEntity>

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun createFragment(position: Int): Fragment {
        val id = noteList[position].id
        return NoteViewFragment.getInstance(id)

    }


}