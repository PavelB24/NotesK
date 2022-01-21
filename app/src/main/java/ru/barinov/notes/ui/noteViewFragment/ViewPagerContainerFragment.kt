package ru.barinov.notes.ui.noteViewFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.*
import ru.barinov.R
import ru.barinov.databinding.NoteviewViewPagerLayoutBinding
import ru.barinov.notes.domain.adapters.NoteViewPagerAdapter
import ru.barinov.notes.domain.adapters.ViewPagerTransformer

import ru.barinov.notes.ui.noteListFragment.NoteListViewModel
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

    private val viewModel by sharedViewModel<NoteListViewModel>()

    private lateinit var binding: NoteviewViewPagerLayoutBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: NoteViewPagerAdapter

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
            val sortedList =list.toMutableList().sortedBy { note -> note.creationTime }
            adapter.noteList = sortedList
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