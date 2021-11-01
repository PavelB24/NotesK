package ru.barinov.notes.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.barinov.R
import ru.barinov.databinding.NoteListLayoutBinding
import ru.barinov.notes.domain.*

class NoteListFragment : Fragment(), OnNoteClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotesAdapter
    private lateinit var binding: NoteListLayoutBinding
    private lateinit var repository: NotesRepository
    private lateinit var toolbar: Toolbar
    private var savedData: Bundle? = null
    private lateinit var data: Bundle


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
        getResults()

    }

    private fun getResults() {
        parentFragmentManager.setFragmentResultListener(NoteEditFragment::class.simpleName!!,requireActivity(), FragmentResultListener { requestKey, result ->
            if(!(result.isEmpty)){
                var tempNote: NoteEntity = result.getParcelable(NoteEntity::class.simpleName)!!
                if (!repository.findById(tempNote.id)) {
                    repository.addNote(tempNote)
                } else {
                    repository.updateNote(tempNote.id, tempNote)
                }
                adapter.data= repository.getNotes()
            }

            })
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
        adapter.data = repository.getNotes()
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
        recyclerView.layoutManager= LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun initRepository() {
        Log.d("@@@", "инициирую")
            Log.d("@@@", "Дата есть")
        repository = savedData!!.getParcelable<NotesRepository>(NotesRepository::class.simpleName)!!
            Log.d("@@@", repository.toString())

    }



    companion object {
        fun getInstance(data: Bundle?): NoteListFragment {
            val noteListInstance = NoteListFragment()
            noteListInstance.arguments = data
            return noteListInstance
        }
    }

    override fun onClickEdit(note: NoteEntity?) {
        Toast.makeText(activity, R.string.edition_mode_toast_text, Toast.LENGTH_SHORT).show()
        data = Bundle()
        data.putParcelable(NoteEntity::class.simpleName, note)
    }

    override fun onClickDelete(note: NoteEntity) {
        TODO("Not yet implemented")
    }

    override fun onNoteClick(note: NoteEntity) {
        data = Bundle()
        data.putParcelable(NoteEntity::class.java.canonicalName, note)
        (requireActivity() as Callable).callNoteViewFragment(data)
    }

    override fun onNoteLongClick(note: NoteEntity, view: View) {
        TODO("Not yet implemented")
    }
}

