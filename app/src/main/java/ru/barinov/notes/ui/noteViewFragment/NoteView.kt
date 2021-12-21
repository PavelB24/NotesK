package ru.barinov.notes.ui.noteViewFragment

import android.content.res.Configuration
import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import ru.barinov.R
import ru.barinov.databinding.NoteViewFramentLayoutBinding
import ru.barinov.notes.domain.ViewModelsFactories
import ru.barinov.notes.ui.application
import ru.barinov.notes.ui.dialogs.MapsFragment
import ru.barinov.notes.ui.notesActivity.Activity

class NoteView : Fragment(), NoteViewContract.ViewInterface {
    private lateinit var binding: NoteViewFramentLayoutBinding
    private lateinit var title: TextView
    private lateinit var body: TextView
    private lateinit var date: TextView
    private lateinit var location: TextView
    private lateinit var viewModel: NoteViewViewModel
    private lateinit var backButton: Button
    private lateinit var mapButton: MaterialButton
    var lat = 0.0
    var lng = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NoteViewFramentLayoutBinding.inflate(inflater, container, false)
        (requireActivity() as Activity).bottomAppBar.backgroundTint = ContextCompat.getColorStateList(requireContext(), R.color.deep_blue_3)
        requireActivity().window.statusBarColor= activity?.resources!!.getColor(R.color.deep_blue_2)
        requireActivity().window.navigationBarColor= activity?.resources!!.getColor(R.color.deep_blue_3)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity(), ViewModelsFactories.NoteViewViewModelFactory
            (requireActivity().application().repository, requireActivity().application().router, requireActivity().application().locationFinder))
            .get(NoteViewViewModel::class.java)
        viewModel.getNote()
        initViews()

        viewModel.openedNote.observe(requireActivity()){ it->
            title.text=it[0]
            body.text=it[1]
            date.text=it[2]
            location.text=it[3]

        }
        super.onViewCreated(view, savedInstanceState)
    }


    private fun initViews() {
        title = binding.noteTitleTextView
        body = binding.noteDescriptionTextview
        date = binding.dateOfEventTextview
        location= binding.locationTextView
        mapButton= binding.showOnMapButton
        initBackButton()
        initMapClickListener()
    }

    private fun initMapClickListener() {
        Log.d("@@@1", "тут1")
        viewModel.latLong.observe(requireActivity()){
            Log.d("@@@1", "тут2")
            lat= it[0]
            lng= it[1]
        }
        mapButton.setOnClickListener {
            val mapFragment = MapsFragment.getInstance(lat, lng)
            mapFragment.show(childFragmentManager, dialogKey)
        }


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





    companion object{
        const val dialogKey = "Map"
        const val latitude = "Latitude"
        const val longitude = "Longitude"
    }
}
