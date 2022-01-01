package ru.barinov.notes.ui.noteViewFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.barinov.databinding.NoteviewViewPagerLayoutBinding
import ru.barinov.notes.domain.NoteViewPagerAdapter
import ru.barinov.notes.ui.application

class ViewPagerContainerFragment: Fragment() {
    private lateinit var  binding: NoteviewViewPagerLayoutBinding
    private lateinit var  viewPager: ViewPager2
    private lateinit var  adapter: NoteViewPagerAdapter
    private lateinit var  tabLayout: TabLayout


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= NoteviewViewPagerLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager= binding.viewPager2
        adapter = NoteViewPagerAdapter(this, requireContext().application().router)
        adapter.noteList = requireContext().application().repository.getNotes()
        viewPager.adapter= adapter


        tabLayout= binding.tabsView
        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            val note = adapter.noteList[position]
            tab.text= note.title
        }.attach()
//        parentFragmentManager.setFragmentResultListener("OnNoteSelected", requireActivity(), { requestKey, result ->
//            val position = findSelectedItemPosition(result.getString("OnNoteSelected")!!)
//            viewPager.currentItem= position
//        })
        super.onViewCreated(view, savedInstanceState)
    }

    private fun findSelectedItemPosition(id: String): Int {
       for(i in adapter.noteList.indices) {
           if (adapter.noteList[i].id==id){
               Log.d("@@@3", "findSelectedItemPosition: $i")
               return i
           }
       }
        return 0
    }
}