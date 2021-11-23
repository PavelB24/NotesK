package ru.barinov.notes.ui.noteViewFragment

import android.content.res.Configuration
import android.os.Bundle
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.barinov.R
import ru.barinov.databinding.NoteViewFramentLayoutBinding
import ru.barinov.notes.ui.Application
import ru.barinov.notes.ui.application
import ru.barinov.notes.ui.notesActivity.Activity

class NoteView : Fragment(), NoteViewContract.ViewInterface {
    private lateinit var binding: NoteViewFramentLayoutBinding
    private lateinit var title: TextView
    private lateinit var body: TextView
    private lateinit var date: TextView
    private var presenter = NoteViewPresenter()
    private lateinit var backButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NoteViewFramentLayoutBinding.inflate(inflater, container, false)
        (requireActivity() as Activity).bottomNavigationItemView.setBackgroundColor(resources.getColor(
            R.color.blue
        ))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.onAttach(this, requireActivity().application().repository, getIdFromRouter()!!)
        requireActivity().application().router.resetId()
        initViews()
        presenter.getNote()
        super.onViewCreated(view, savedInstanceState)
    }


    private fun initViews() {
        title = binding.noteTitleTextView
        body = binding.noteDescriptionTextview
        date = binding.dateOfEventTextview
        initBackButton()
    }

    private fun initBackButton() {
        backButton = binding.noteViewFragmentBackButton
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

    }

    override fun fillTheFields(noteTitle: String, detail: String, dateAsString: String) {
        title.text = noteTitle
        body.text = detail
        date.text = dateAsString
        Linkify.addLinks(body, Linkify.ALL)
        body.setLinkTextColor(resources.getColor(R.color.blue))

    }

    override fun onPause() {
        parentFragmentManager.popBackStackImmediate()
        super.onPause()
    }

    override fun onDestroy() {
        if(requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            (requireActivity() as Activity).bottomNavigationItemView.setBackgroundColor(resources.getColor(R.color.cherry))
        }
        presenter.onDetach()
        super.onDestroy()
    }
    private fun getIdFromRouter(): String?{
        return (requireActivity().application()).router.getId()



//    companion object {
//        fun getInstance(data: Bundle): NoteViewFragment {
//            val noteViewInstance = NoteViewFragment()
//            noteViewInstance.arguments = data
//            return noteViewInstance
//        }
}}
