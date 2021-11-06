package ru.barinov.notes.ui.noteViewFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.barinov.databinding.NoteViewFramentLayoutBinding
import ru.barinov.notes.domain.NoteEntity

class NoteViewFragment: Fragment() {
    private lateinit var binding: NoteViewFramentLayoutBinding
    private lateinit var note: NoteEntity
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var date: TextView
    private lateinit var backButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=NoteViewFramentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        toFillViews()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun toFillViews() {
        getNoteFromArg()
        title.text = note.title
        description.text = note.detail
        date.text = note.dateAsString
    }

    private fun getNoteFromArg() {
        val data = arguments
        note= data?.getParcelable<NoteEntity>(NoteEntity::class.simpleName)!!
    }

    private fun initViews() {
        title= binding.noteTitleTextView
        description= binding.noteDescriptionTextview
        date = binding.dateOfEventTextview
        initBackButton()
    }

    private fun initBackButton() {
        backButton= binding.noteViewFragmentBackButton
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

    }


    companion object {
        fun getInstance(data: Bundle): NoteViewFragment {
            val noteViewInstance = NoteViewFragment()
            noteViewInstance.arguments = data
            return noteViewInstance
        }
    }
}