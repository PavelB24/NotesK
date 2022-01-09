package ru.barinov.notes.ui.noteViewFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.barinov.R
import ru.barinov.databinding.NoteviewViewPagerLayoutBinding
import ru.barinov.notes.domain.adapters.NoteViewPagerAdapter
import ru.barinov.notes.domain.adapters.ViewPagerTransformer
import ru.barinov.notes.domain.entity.NoteEntity
import ru.barinov.notes.ui.application
import ru.barinov.notes.ui.noteEditFragment.argsBundleKey

class ViewPagerContainerFragment : Fragment() {

    private lateinit var binding: NoteviewViewPagerLayoutBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: NoteViewPagerAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var observer: LiveData<List<NoteEntity>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        observer = requireContext().application().repository.getNotesLiveData()
        binding = NoteviewViewPagerLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = binding.viewPager2
        adapter = NoteViewPagerAdapter(this)
        //todo не получает данные
        registeredForGetNotesLiveData()
        viewPager.adapter = adapter
        viewPager.setPageTransformer(ViewPagerTransformer())



        tabLayout = binding.tabsView
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val note = adapter.noteList[position]
            tab.text = note.title
            if (note.isFavorite) {
                tab.setIcon(R.drawable.ic_favourites_selected_star)
            }
        }.attach()
//        viewPager.post {
//            viewPager.currentItem =
//                    //todo data iz args
//                findSelectedItemPosition( )
//            requireContext().application().router.resetId()

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

    private fun registeredForGetNotesLiveData() {
        observer.observe(viewLifecycleOwner) { notes ->
            adapter.noteList = notes.toMutableList()
            adapter.notifyDataSetChanged()
        }
    }

    private fun findSelectedItemPosition(id: String): Int {
        for (i in adapter.noteList.indices) {
            if (adapter.noteList[i].id == id) {
                return i
            }
        }
        return 0
    }

    companion object {

        fun getInstance(id: String?): ViewPagerContainerFragment {
            val fragment = ViewPagerContainerFragment()
            val data = Bundle()
            data.putString(argsBundleKey, id)
            fragment.arguments = data
            return fragment
        }
    }

}