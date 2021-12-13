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
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import ru.barinov.R
import ru.barinov.databinding.NoteViewFramentLayoutBinding
import ru.barinov.notes.domain.ViewModelsFactories
import ru.barinov.notes.ui.application
import ru.barinov.notes.ui.notesActivity.Activity

class NoteView : Fragment(), NoteViewContract.ViewInterface {
    private lateinit var binding: NoteViewFramentLayoutBinding
    private lateinit var title: TextView
    private lateinit var body: TextView
    private lateinit var date: TextView
    private lateinit var location: TextView
    private lateinit var viewModel: NoteViewViewModel
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
        initBackButton()
        initLocationTextViewClicker()
    }

    private fun initLocationTextViewClicker() {
        location.setOnClickListener {
            var lat = 0.0
            var lng = 0.0
            viewModel.createDialog()
            viewModel.latLong.observe(requireActivity()){
                lat= it[0]
                lng= it[1]
            }
            viewModel.onMapDialogCreated.observe(requireActivity()){
                val latlng= com.google.android.gms.maps.model.LatLng(lat, lng)
                registerForACallback{
                    it.moveCamera(CameraUpdateFactory.newLatLng(latlng))
                }
                it.show(parentFragmentManager, dialogKey )

            }
        }
    }
    private fun registerForACallback(callback: OnMapReadyCallback) {
        val mapFragment = parentFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
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



    override fun onDestroy() {
        if(requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            (requireActivity() as Activity).bottomNavigationItemView.setBackgroundColor(resources.getColor(R.color.cherry))
        }
        super.onDestroy()
    }

    companion object{
        const val dialogKey = "Map"
    }
}
