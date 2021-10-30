package ru.barinov.notes.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.barinov.R
import ru.barinov.databinding.NoteListLayoutBinding
import ru.barinov.notes.domain.*

class NoteListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotesAdapter
    private lateinit var binding: NoteListLayoutBinding
    private lateinit var repository: NotesRepository
    private lateinit var toolbar: Toolbar
    private var savedData: Bundle? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = NoteListLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedData = arguments
        initFragmentsContent()

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (!menu.hasVisibleItems()) {
            inflater.inflate(R.menu.notes_list_menu, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_note_item_menu -> {
                savedData= null
                (requireActivity() as Callable).callEditionFragment(savedData)
                //TODO
                return true
            }
            R.id.refresh_item_menu -> {
                adapter.data = repository.allNotes
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initFragmentsContent() {
        initRepository()
        setAdapter()
        setRecyclerView()
        setToolbar()
    }

    private fun setToolbar() {
        toolbar = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    private fun setAdapter() {
        adapter = NotesAdapter()
        adapter.data = repository.allNotes
        adapter.setListener(object: OnNoteClickListener{
            override fun onClickEdit(note: NoteEntity?) {
                TODO("Not yet implemented")
            }

            override fun onClickDelete(note: NoteEntity) {
                TODO("Not yet implemented")
            }

            override fun onNoteClick(note: NoteEntity) {
                TODO("Not yet implemented")
            }

            override fun onNoteLongClick(note: NoteEntity, view: View) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setRecyclerView() {
        recyclerView = binding.recyclerview
        recyclerView.layoutManager
        recyclerView.adapter = adapter
    }

    private fun initRepository() {
        repository = savedData?.getParcelable<NotesRepository>(NotesRepository::class.simpleName)!!
    }

    companion object {
        fun getInstance(data: Bundle?): NoteEditFragment {
            val noteListInstance = NoteEditFragment()
            noteListInstance.arguments = data
            return noteListInstance
        }
    }
}

