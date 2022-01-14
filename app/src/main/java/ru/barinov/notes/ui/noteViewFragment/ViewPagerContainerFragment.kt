package ru.barinov.notes.ui.noteViewFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.R
import ru.barinov.databinding.NoteviewViewPagerLayoutBinding
import ru.barinov.notes.domain.adapters.NoteViewPagerAdapter
import ru.barinov.notes.domain.adapters.ViewPagerTransformer
import ru.barinov.notes.domain.models.NoteEntity
import ru.barinov.notes.ui.application
import ru.barinov.notes.ui.noteEditFragment.argsBundleKey
import java.lang.IllegalStateException

private const val argumentsKey = "viewPagerArgsKey"

class ViewPagerContainerFragment : Fragment() {

    private val noteId: String by lazy {
        val id = requireArguments().getString(argumentsKey)
        if (id == null) {
            throw IllegalStateException("ID should be provided")
        }
        return@lazy id
    }

    private val viewModel by viewModel<ViewPagerContainerFragmentViewModel>()

    private lateinit var binding: NoteviewViewPagerLayoutBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: NoteViewPagerAdapter
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = NoteviewViewPagerLayoutBinding.inflate(inflater)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initViews()
        viewPager.adapter = adapter
        tabLayout = binding.tabsView
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val note = adapter.noteList[position]
            tab.text = note.title
            if (note.isFavorite) {
                tab.setIcon(R.drawable.ic_favourites_selected_star)
            }
        }.attach()
        viewPager.currentItem = findSelectedItemPosition(noteId)


        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViews() {
        adapter = NoteViewPagerAdapter(this)
        viewPager = binding.viewPager2
        registeredForGetNotesLiveData()
        viewPager.setPageTransformer(ViewPagerTransformer())
        viewPager.post {  viewPager.currentItem = findSelectedItemPosition(noteId) }

    }


    private fun registeredForGetNotesLiveData() {
        viewModel.noteListLiveData.observe(viewLifecycleOwner) { list ->
            adapter.noteList = list
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
            data.putString(argumentsKey, id)
            fragment.arguments = data
            return fragment
        }
    }

}