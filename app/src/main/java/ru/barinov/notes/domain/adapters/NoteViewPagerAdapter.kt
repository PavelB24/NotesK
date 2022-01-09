package ru.barinov.notes.domain.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.barinov.notes.domain.entity.NoteEntity
import ru.barinov.notes.ui.noteViewFragment.NotePageFragment

class NoteViewPagerAdapter( fragment: Fragment): FragmentStateAdapter(fragment) {
     var noteList = emptyList<NoteEntity>()

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun createFragment(position: Int): Fragment {
        val id = noteList[position].id
        return NotePageFragment.getInstance(id)

    }


}