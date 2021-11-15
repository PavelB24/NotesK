package ru.barinov.notes.ui.noteListFragment

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.barinov.R
import ru.barinov.databinding.NoteListLayoutBinding
import ru.barinov.notes.domain.*
import ru.barinov.notes.ui.AgreementDialogFragment
import ru.barinov.notes.ui.notesActivity.NotesActivity

class NoteListFragment : Fragment(), NoteListFragmentContract.View {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotesAdapter
    private lateinit var binding: NoteListLayoutBinding
    private lateinit var toolbar: Toolbar
    private lateinit var searchItem: MenuItem
    private lateinit var presenter: NoteListFragmentPresenter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = NoteListLayoutBinding.inflate(inflater, container, false)
        presenter = NoteListFragmentPresenter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.onAttach(this)
        (requireActivity() as NotesActivity).bottomNavigationItemView.setBackgroundColor(resources.getColor(R.color.toolbar_grey))
        initViews()
        presenter.getResultsFromNoteEditFragment(adapter)
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (!menu.hasVisibleItems()) {
            inflater.inflate(R.menu.notes_list_menu, menu)
            searchItem = menu.findItem(R.id.search_item_menu)
            val searchView = searchItem.actionView as android.widget.SearchView
            presenter.onSearchStarted(searchView)
            super.onCreateOptionsMenu(menu, inflater)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_note_item_menu -> {
                presenter.createNewNote()
            }
            R.id.delete_chosen_notes -> {
                presenter.deleteChosenNotes()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initViews() {
        initAdapter()
        setRecyclerView()
        setToolbar()
    }

    private fun setToolbar() {
        toolbar = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    private fun initAdapter() {
        adapter = NotesAdapter()
        presenter.setAdapter(adapter)
    }

    private fun setRecyclerView() {
        recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    override fun searchWasUnsuccessfulMessage() {
        Toast.makeText(view?.context, R.string.unsuccessful_search_toast_text, Toast.LENGTH_SHORT)
            .show()
    }

    override fun onEditionModeToastMessage() {
        Toast.makeText(activity, R.string.edition_mode_toast_text, Toast.LENGTH_SHORT).show()
    }
}

