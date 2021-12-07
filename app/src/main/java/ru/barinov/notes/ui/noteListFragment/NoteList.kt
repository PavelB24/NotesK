package ru.barinov.notes.ui.noteListFragment

import android.os.Bundle
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
import ru.barinov.notes.domain.Callable
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.noteEntityAndService.NotesAdapter
import ru.barinov.notes.domain.room.DataBase
import ru.barinov.notes.ui.AgreementDialogFragment
import ru.barinov.notes.ui.application
import ru.barinov.notes.ui.noteEditFragment.NoteEdit
import ru.barinov.notes.ui.notesActivity.Activity

class NoteList : Fragment(){
    private lateinit var recyclerView: RecyclerView
    private val adapter= NotesAdapter()
    private lateinit var binding: NoteListLayoutBinding
    private lateinit var toolbar: Toolbar
    private lateinit var searchItem: MenuItem
    private lateinit var presenter: NoteListViewModel
    private val DELETE = "OK"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = NoteListLayoutBinding.inflate(inflater, container, false)
        presenter = NoteListViewModel(getRepository(), adapter, requireActivity().application().cache, getLocalDB(), requireActivity().application().authentication,
        getCloudDB(), (requireActivity() as Callable), requireActivity().application().router)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as Activity).bottomNavigationItemView.setBackgroundColor(resources.getColor(R.color.cherry))
        initViews()
        parentFragmentManager.setFragmentResultListener(
            NoteEdit::class.simpleName!!,
            requireActivity(),
            FragmentResultListener { requestKey, result ->
                presenter.getResultsFromNoteEditFragment(result)
                })
        createDialog()
        searchWasUnsuccessfulMessage()
        onEditionModeToastMessage()
        super.onViewCreated(view, savedInstanceState)

    }

    private fun createDialog() {
        presenter.onNoteDeletion.observe(requireActivity()) { it ->
            it.show(parentFragmentManager, DELETE)
            parentFragmentManager.setFragmentResultListener(
                AgreementDialogFragment::class.simpleName!!,
                requireActivity(), { requestKey, result ->
                    presenter.deleteNoteInRepos(result, DELETE)
                })
        }
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
        setRecyclerView()
        setToolbar()
    }

    private fun setToolbar() {
        toolbar = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }



    private fun setRecyclerView() {
        presenter.setAdapter()
        recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }



    private fun searchWasUnsuccessfulMessage() {
        presenter.onUnsuccessfulSearch.observe(requireActivity()){
            Toast.makeText(view?.context, R.string.unsuccessful_search_toast_text, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun onEditionModeToastMessage() {
        presenter.editionModeMessage.observe(requireActivity()){
        Toast.makeText(activity, R.string.edition_mode_toast_text, Toast.LENGTH_SHORT).show()
        }
    }
    private fun getRepository(): NotesRepository {
        return requireActivity().application().repository
    }

    private fun getLocalDB(): DataBase {
        return requireActivity().application().localDataBase
    }
    private fun getCloudDB(): CloudRepository {
        return requireActivity().application().cloudDataBase
    }
}

