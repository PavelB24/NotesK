package ru.barinov.notes.ui.noteViewFragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.barinov.R
import ru.barinov.databinding.NoteViewFramentLayoutBinding
import ru.barinov.notes.ui.dialogs.MapsFragment
import java.lang.IllegalStateException

private const val argumentsKey = "argumentsKey"

class NotePageFragment : Fragment() {

    private val noteId: String by lazy {
        val id = requireArguments().getString(argumentsKey)
        if (id == null) {
            throw IllegalStateException("ID should be provided")
        }
        return@lazy id
    }

    private val viewModel by viewModel<NotePageViewModel> { parametersOf(noteId) }
    private lateinit var binding: NoteViewFramentLayoutBinding
    private lateinit var titleTextView: TextView
    private lateinit var contentTextview: TextView
    private lateinit var dateTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var mapButton: MaterialButton
    private lateinit var noteDraft: NoteDraftExtended
    private lateinit var image: ShapeableImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = NoteViewFramentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initViews()
        setListeners()
        viewModel.getNote()
        initMapClickListener()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadImage() {
        if (noteDraft.image.isNotEmpty()) {
            image.visibility = View.VISIBLE
            Thread {
                val bitmap =BitmapFactory.decodeByteArray(noteDraft.image, 0, noteDraft.image.size)
                requireActivity().runOnUiThread{
                    image.setImageBitmap(bitmap)
                }

            }.start()
        }
    }

    private fun setListeners() {
        viewModel.openedNoteDraft.observe(viewLifecycleOwner) { draft ->
            titleTextView.text = draft.title
            contentTextview.text = draft.content
            dateTextView.text = draft.date
            locationTextView.text = draft.location
            Linkify.addLinks(contentTextview, Linkify.ALL)
            contentTextview.setLinkTextColor(resources.getColor(R.color.blue))
            noteDraft = draft
            loadImage()
        }
    }

    private fun initViews() {
        image = binding.notePageImgView
        titleTextView = binding.noteTitleTextView
        contentTextview = binding.noteDescriptionTextview
        dateTextView = binding.dateOfEventTextview
        locationTextView = binding.locationTextView
        mapButton = binding.showOnMapButton

    }

    private fun initMapClickListener() {
        mapButton.setOnClickListener {
            val mapFragment = MapsFragment.getInstance(noteDraft.latitude, noteDraft.longitude)
            mapFragment.show(childFragmentManager, dialogKey)
        }

    }

    companion object {

        const val dialogKey = "Map"
        const val latitude = "Latitude"
        const val longitude = "Longitude"

        fun getInstance(id: String): Fragment {
            val fragment = NotePageFragment()
            val bundle = Bundle()
            bundle.putString(argumentsKey, id)
            fragment.arguments = bundle
            return fragment
        }
    }
}
