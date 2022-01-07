package ru.barinov.notes.ui.noteListFragment

import android.content.SharedPreferences
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
import ru.barinov.notes.ui.dialogs.AgreementDialogFragment
import ru.barinov.notes.ui.application
import ru.barinov.notes.ui.dialogs.ReminderDialogFragment
import ru.barinov.notes.ui.noteEditFragment.NoteEdit
import ru.barinov.notes.ui.notesActivity.Activity
import androidx.core.content.ContextCompat


class NoteList : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val adapter = NotesAdapter()
    private lateinit var binding: NoteListLayoutBinding
    private lateinit var toolbar: Toolbar
    private lateinit var searchItem: MenuItem
    private lateinit var viewModel: NoteListViewModel
    private lateinit var editor: SharedPreferences.Editor
    private val DELETE = "OK"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = NoteListLayoutBinding.inflate(inflater, container, false)
        viewModel = NoteListViewModel(
            repository = getRepository(),
            adapter = adapter,
            cache = requireActivity().application().cache,
            localDataBase = getLocalDB(),
            authentication = requireActivity().application().authentication,
            cloudDataBase = getCloudDB(),
            activity = (requireActivity() as Callable),
            router = requireActivity().application().router,
            sharedPref = requireContext().application().pref
        )
        requireActivity().window.statusBarColor =
            activity?.resources!!.getColor(R.color.deep_blue_2)
        requireActivity().window.navigationBarColor =
            activity?.resources!!.getColor(R.color.card_view_grey_2)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        editor = getSharedPreferences().edit()
        (requireActivity() as Activity).bottomAppBar.backgroundTint =
            ContextCompat.getColorStateList(requireContext(), R.color.card_view_grey)
        initViews()
        parentFragmentManager.setFragmentResultListener(
            NoteEdit::class.simpleName!!,
            requireActivity(),
            FragmentResultListener { requestKey, result ->
                viewModel.getResultsFromNoteEditFragment(
                    result
                )
            })
        parentFragmentManager.setFragmentResultListener(
            requireActivity().javaClass.simpleName,
            requireActivity(), { requestKey, result ->
                viewModel.refreshAdapter()
            })
        createDialog()
        searchWasUnsuccessfulMessage()
        onEditionModeToastMessage()
        registeredForReminderDialogCreation()
//        registeredForNoteSelected()
        super.onViewCreated(view, savedInstanceState)

    }

    private fun getSharedPreferences(): SharedPreferences {
        return requireActivity().application().pref
    }

//    private fun registeredForNoteSelected() {
//        viewModel.onNoteClicked.observe(requireActivity()){
////            val bundle = Bundle()
////            bundle.putString("OnNoteSelected", it)
////            parentFragmentManager.setFragmentResult("OnNoteSelected", bundle)
//        }
//    }

    private fun registeredForReminderDialogCreation() {
        viewModel.createReminderDialog.observe(requireActivity()) {
            requireActivity().application().router.setId(it)
            val reminderDialog = ReminderDialogFragment()
            reminderDialog.show(childFragmentManager, "Reminder")
        }
    }

    private fun createDialog() {
        viewModel.onNoteDeletion.observe(requireActivity()) { it ->
            it.show(parentFragmentManager, DELETE)
            parentFragmentManager.setFragmentResultListener(
                AgreementDialogFragment::class.simpleName!!,
                requireActivity(), { requestKey, result ->
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
                if (item.isChecked) {
                    viewModel.showOnlyFavs()
                    item.setIcon(R.drawable.ic_favourites_black_star_symbol_icon_icons_com_activated)
                } else {
                    item.setIcon(R.drawable.ic_favourites_black_star_symbol_icon_icons_com_54534)
                    viewModel.cancelShowOnlyFavs()
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
                view?.context,
                R.string.unsuccessful_search_toast_text,
                Toast.LENGTH_SHORT
            )
                .show()
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

    private fun getLocalDB(): DataBase {
        return requireActivity().application().localDataBase
    }

    private fun getCloudDB(): CloudRepository {
        return requireActivity().application().cloudDataBase
    }

    override fun onResume() {
        editor.putBoolean(NoteList::class.java.simpleName, viewModel.checkOnFavoritesView())
        super.onResume()
    }

}

