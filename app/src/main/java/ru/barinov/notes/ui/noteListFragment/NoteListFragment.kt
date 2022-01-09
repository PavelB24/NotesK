package ru.barinov.notes.ui.noteListFragment

import android.content.*
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.barinov.R
import ru.barinov.databinding.NoteListLayoutBinding
import ru.barinov.notes.domain.interfaces.Callable
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.userRepository.NotesRepository
import ru.barinov.notes.domain.adapters.NotesAdapter
import ru.barinov.notes.ui.dialogs.AgreementDialogFragment
import ru.barinov.notes.ui.application
import ru.barinov.notes.ui.dialogs.ReminderDialogFragment
import androidx.lifecycle.ViewModelProvider
import ru.barinov.notes.domain.ViewModelsFactories
import ru.barinov.notes.ui.dataManagerFragment.DataManagerFragment

class NoteListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val adapter = NotesAdapter()
    private lateinit var binding: NoteListLayoutBinding
    private lateinit var toolbar: Toolbar
    private lateinit var searchItem: MenuItem
    private lateinit var viewModel: NoteListViewModel
    private val DELETE = "OK"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = NoteListLayoutBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(
            viewModelStore, ViewModelsFactories.NoteListViewModelFactory(
                repository = getRepository(),
                adapter = adapter,
                cloudDataBase = getCloudDB(),
                activity = (requireActivity() as Callable),
                sharedPref = requireContext().getSharedPreferences(DataManagerFragment.sharedPreferencesName, Context.MODE_PRIVATE)
            )
        )[NoteListViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        registeredForNotesRepositoryLiveData()
        createDialog()
        searchWasUnsuccessfulMessage()
        onEditionModeToastMessage()
        registeredForReminderDialogCreation()
        super.onViewCreated(view, savedInstanceState)

    }

    private fun registeredForNotesRepositoryLiveData() {
        viewModel.noteListLiveData.observe(viewLifecycleOwner) { notes ->
            adapter.data = notes
        }
    }

    private fun registeredForReminderDialogCreation() {
        viewModel.createReminderDialog.observe(requireActivity()) { id ->
            val reminderDialog = ReminderDialogFragment.getInstance(id)
            reminderDialog.show(childFragmentManager, "Reminder")
        }
    }

    private fun createDialog() {
        viewModel.onNoteDeletion.observe(requireActivity()) { it ->
            it.show(parentFragmentManager, DELETE)
            parentFragmentManager.setFragmentResultListener(
                AgreementDialogFragment::class.simpleName!!,
                requireActivity(),
                { requestKey, result ->
                    viewModel.deleteNoteInRepos(result, DELETE)
                })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (!menu.hasVisibleItems()) {
            inflater.inflate(R.menu.notes_list_menu, menu)
            searchItem = menu.findItem(R.id.search_item_menu)
            val searchView = searchItem.actionView as android.widget.SearchView
            viewModel.onSearchStarted(searchView)
            super.onCreateOptionsMenu(menu, inflater)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_chosen_notes -> {
                viewModel.deleteChosenNotes()
            }
            R.id.favorites_menu_button -> {
                item.isChecked = !item.isChecked
                val isChecked = item.isChecked
                viewModel.onFavoriteClicked(isChecked)
                if (isChecked) {
                    item.setIcon(R.drawable.ic_favourites_selected_star)
                } else {
                    item.setIcon(R.drawable.ic_favourites_black_star)
                }
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
        viewModel.setAdapter()
        recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun searchWasUnsuccessfulMessage() {
        viewModel.onUnsuccessfulSearch.observe(requireActivity()) {
            Toast.makeText(
                view?.context, R.string.unsuccessful_search_toast_text, Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onEditionModeToastMessage() {
        viewModel.editionModeMessage.observe(requireActivity()) {
            Toast.makeText(activity, R.string.edition_mode_toast_text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getRepository(): NotesRepository {
        return requireActivity().application().repository
    }

    private fun getCloudDB(): CloudRepository {
        return requireActivity().application().cloudDataBase
    }

}

