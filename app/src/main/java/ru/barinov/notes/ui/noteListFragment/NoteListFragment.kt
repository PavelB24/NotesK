package ru.barinov.notes.ui.noteListFragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.*
import ru.barinov.R
import ru.barinov.databinding.NoteListLayoutBinding
import ru.barinov.notes.domain.adapters.NotesAdapter
import ru.barinov.notes.ui.dialogs.AgreementDialogFragment
import ru.barinov.notes.ui.dialogs.ReminderDialogFragment
import ru.barinov.notes.domain.interfaces.ActivityCallableInterface
import org.koin.java.KoinJavaComponent.inject

private const val delete = "OK"
private const val favButtonKey = "favButton"
private const val reminderDialogKey = "reminder"

class NoteListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val adapter = NotesAdapter()
    private lateinit var binding: NoteListLayoutBinding
    private lateinit var toolbar: Toolbar
    private lateinit var searchItem: MenuItem
    private val sharedPreferences = inject<SharedPreferences>(SharedPreferences::class.java)
    private lateinit var editor: SharedPreferences.Editor
    private val viewModel by sharedViewModel<NoteListViewModel>()

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?

    ): View {
        editor = sharedPreferences.value.edit()
        setHasOptionsMenu(true)
        binding = NoteListLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        registeredForListeners()
        createDialog()
        searchWasUnsuccessfulMessage()
        registeredForReminderDialogCreation()
        super.onViewCreated(view, savedInstanceState)

    }

    private fun registeredForListeners() {
        viewModel.noteListLiveData.observe(viewLifecycleOwner) { notes ->
            val sortedList = notes.toMutableList().sortedBy { note -> note.creationTime }
            adapter.data = sortedList
        }


        viewModel.onNoteOpen.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                (requireActivity() as ActivityCallableInterface).callNoteViewFragment(it)
            }
        })



        viewModel.onEditClicked.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                (requireActivity() as ActivityCallableInterface).callEditionFragment(it)
            }
        }
    }

    private fun registeredForReminderDialogCreation() {
        viewModel.createReminderDialog.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { id ->
                val reminderDialog = ReminderDialogFragment.getInstance(id)
                reminderDialog.show(childFragmentManager, reminderDialogKey)
            }
        }
    }

    private fun createDialog() {
        viewModel.onNoteDeletion.observe(viewLifecycleOwner) { dialog ->
            dialog.show(parentFragmentManager, delete)
            parentFragmentManager.setFragmentResultListener(AgreementDialogFragment::class.simpleName!!,
                requireActivity(),
                { requestKey, result ->
                    viewModel.deleteNoteInRepos(result, delete)
                })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (!menu.hasVisibleItems()) {
            inflater.inflate(R.menu.notes_list_menu, menu)
            searchItem = menu.findItem(R.id.search_item_menu)
            val searchView = searchItem.actionView as android.widget.SearchView
            viewModel.onSearchStarted(searchView)

            val favButton = menu.findItem(R.id.favorites_menu_button)
            favButton.isChecked = sharedPreferences.value.getBoolean(favButtonKey, false)
            viewModel.onFavoriteClicked(favButton.isChecked)
            if (favButton.isChecked) {
                favButton.setIcon(R.drawable.ic_favourites_selected_star)
            } else {
                favButton.setIcon(R.drawable.ic_favourites_black_star)
            }

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
                saveFavButtonState(item.isChecked)
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

    private fun saveFavButtonState(state: Boolean) {
        editor.putBoolean(favButtonKey, state).apply()
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
        viewModel.setAdapter(adapter)
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

}

