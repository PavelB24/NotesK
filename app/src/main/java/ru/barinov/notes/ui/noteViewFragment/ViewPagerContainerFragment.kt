package ru.barinov.notes.ui.noteViewFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.barinov.R
import ru.barinov.databinding.NoteviewViewPagerLayoutBinding
import ru.barinov.notes.domain.NoteViewPagerAdapter
import ru.barinov.notes.domain.ViewPagerTransformer
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity
import ru.barinov.notes.ui.application
import ru.barinov.notes.ui.noteEditFragment.NoteEdit

class ViewPagerContainerFragment : Fragment() {
    private lateinit var binding: NoteviewViewPagerLayoutBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: NoteViewPagerAdapter
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NoteviewViewPagerLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = binding.viewPager2
        adapter = NoteViewPagerAdapter(this)
        adapter.noteList = requireContext().application().repository.getNotes()
        viewPager.adapter = adapter
        viewPager.setPageTransformer(ViewPagerTransformer())



        tabLayout = binding.tabsView
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val note = adapter.noteList[position]
            tab.text = note.title
            if (note.isFavorite) {
                tab.setIcon(R.drawable.ic_favourites_black_star_symbol_icon_icons_com_activated)
            }
        }.attach()
        viewPager.post {
            viewPager.currentItem =
                findSelectedItemPosition(requireContext().application().router.getId()!!)
            requireContext().application().router.resetId()
        }

//
//Todo Почем не работает?
//        parentFragmentManager.setFragmentResultListener("OnNoteSelected", requireActivity(), { requestKey, result ->
//            val selectedPosition =findSelectedItemPosition(result.getString("OnNoteSelected")!!)
//            Log.d("@@@6", selectedPosition.toString() )
//            viewPager.currentItem=selectedPosition
//            Log.d("@@@7", viewPager.currentItem.toString() )
//        })


        super.onViewCreated(view, savedInstanceState)
    }


    private fun findSelectedItemPosition(id: String): Int {
        for (i in adapter.noteList.indices) {
            if (adapter.noteList[i].id == id) {
                return i
            }
        }
        return 0
    }


}