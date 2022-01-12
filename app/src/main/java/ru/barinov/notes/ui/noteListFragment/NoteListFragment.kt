package ru.barinov.notes.ui.noteListFragment

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
import ru.barinov.notes.domain.adapters.NotesAdapter
import ru.barinov.notes.ui.dialogs.AgreementDialogFragment
import ru.barinov.notes.ui.dialogs.ReminderDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.barinov.notes.domain.interfaces.ActivityCallableInterface
import ru.barinov.notes.ui.notesActivity.ActivityMain

private const val DELETE = "OK"


class NoteListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val adapter = NotesAdapter()
    private lateinit var binding: NoteListLayoutBinding
    private lateinit var toolbar: Toolbar
    private lateinit var searchItem: MenuItem
    private  val viewModel by viewModel<NoteListViewModel>{ parametersOf(adapter, (requireActivity() as ActivityCallableInterface))}

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?

    ): View {

        setHasOptionsMenu(true)
        binding = NoteListLayoutBinding.inflate(inflater, container, false)
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

            //  todo Перенести в активити вью модель
            val navigationView =  (requireActivity() as ActivityMain).bottomNavigationView
            navigationView.getOrCreateBadge(R.id.notes_item_menu)
            val badge = navigationView.getBadge(R.id.notes_item_menu)
            badge!!.number = adapter.data.size
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



}

