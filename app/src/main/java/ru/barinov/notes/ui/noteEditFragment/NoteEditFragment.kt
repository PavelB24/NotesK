package ru.barinov.notes.ui.noteEditFragment

import android.Manifest
import android.content.*

import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.barinov.R
import ru.barinov.databinding.NoteEditLayoutBinding
import android.provider.MediaStore
import android.app.Activity.RESULT_OK
import android.graphics.*
import kotlinx.coroutines.*
import android.net.Uri
import androidx.core.view.drawToBitmap
import com.google.android.material.chip.Chip
import kotlinx.coroutines.GlobalScope
import ru.barinov.notes.domain.models.NoteTypes
import java.io.ByteArrayOutputStream
import java.lang.RuntimeException

const val argsBundleKey = "NotesId"
const val REQUEST_CODE = 100

@DelicateCoroutinesApi
class NoteEditFragment : Fragment() {

    private lateinit var applyButton: Button
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var image: AppCompatImageView
    private lateinit var binding: NoteEditLayoutBinding
    private var bitmapArray: ByteArray = byteArrayOf()

    private lateinit var noteTypeChip: Chip
    private lateinit var toDoListTypeChip: Chip
    private lateinit var pictureTypeChip: Chip
    private var selectedType: NoteTypes = NoteTypes.Note

    private var tempNoteId: String? = ""

    private val viewModel by viewModel<NoteEditViewModel> { parametersOf(tempNoteId) }

    override fun onAttach(context: Context) {
        tempNoteId = requireArguments().getString(argsBundleKey)
        if (tempNoteId == null) {
            tempNoteId = ""
        }
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = NoteEditLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initViews()

        val permission = ActivityCompat.checkSelfPermission(
            requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PERMISSION_GRANTED
        if (permission) {
            viewModel.startListenLocation()
        }

        viewModel.fillTheViews()

        registeredForListeners()

        initOnClickActions()
        viewModel.fieldsIsNotFilledMassageLiveData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(activity, R.string.warning_toast, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.closeScreenViewModel.observe(requireActivity()) {
            parentFragmentManager.popBackStackImmediate()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun registeredForListeners() {
        viewModel.fillViewWithData.observe(viewLifecycleOwner) { draft ->
            titleEditText.setText(draft.title)
            contentEditText.setText(draft.content)

            if (draft.image.isNotEmpty()) {

                bitmapArray = draft.image
                GlobalScope.launch(Dispatchers.Default) {
                    val bitmapArray = BitmapFactory.decodeByteArray(draft.image, 0, draft.image.size)

                    withContext(Dispatchers.Main) {
                        image.setImageBitmap(bitmapArray)
                    }
                }

            }
            when (draft.type) {
                NoteTypes.Note -> noteTypeChip.isChecked = true
                NoteTypes.ToDoList -> toDoListTypeChip.isChecked = true
                NoteTypes.Photo -> pictureTypeChip.isChecked = true
                NoteTypes.Idle -> noteTypeChip.isChecked = true
            }
        }
        viewModel.editionModeMessage.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(activity, R.string.edition_mode_toast_text, Toast.LENGTH_SHORT).show()
                if (bitmapArray.isNotEmpty()) {
                    binding.imageRotationButtonsContainer!!.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initViews() {

        image = binding.noteEditImgView!!
        titleEditText = binding.titleEdittext
        contentEditText = binding.descriptionEdittext
        noteTypeChip = binding.noteChip!!
        toDoListTypeChip = binding.toDoListChip!!
        pictureTypeChip = binding.photoChip!!
        binding.rotateLeftImageButton!!.setOnClickListener {
            val matrix = Matrix()
            matrix.postRotate(image.rotation - 90f)
            image.rotation = (image.rotation - 90f)
            val idleBitmap = image.drawToBitmap()
            GlobalScope.launch(Dispatchers.Default) {
                val rotatedImgBitmap =
                    Bitmap.createBitmap(idleBitmap, 0, 0, idleBitmap.width, idleBitmap.height, matrix, true)
                bitmapToByteArray(rotatedImgBitmap)
            }

        }
        binding.rotateRightImageButton!!.setOnClickListener {
            val matrix = Matrix()
            matrix.postRotate(image.rotation + 90f)
            image.rotation = (image.rotation + 90f)
            val idleBitmap = image.drawToBitmap()
            GlobalScope.launch(Dispatchers.Default) {
                val rotatedImgBitmap =
                    Bitmap.createBitmap(idleBitmap, 0, 0, idleBitmap.width, idleBitmap.height, matrix, true)
                bitmapToByteArray(rotatedImgBitmap)
            }

        }
        initChipLogic()
    }

    private fun initChipLogic() {
        noteTypeChip.setOnClickListener {
            selectedType = NoteTypes.Note
            contentEditText.visibility = View.VISIBLE
        }
        toDoListTypeChip.setOnClickListener {
            selectedType = NoteTypes.ToDoList
            contentEditText.visibility = View.VISIBLE
        }
        pictureTypeChip.setOnClickListener {
            selectedType = NoteTypes.Photo
            contentEditText.visibility = View.GONE
        }
    }

    private fun initOnClickActions() {
        applyButton = binding.applyButton as Button
        applyButton.setOnClickListener {
            viewModel.saveNote(
                draft = NoteDraft(
                    titleEditText.text.toString(), contentEditText.text.toString(), selectedType, bitmapArray
                )
            )
        }
        image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Select pic"), REQUEST_CODE)
        }
        image.setOnLongClickListener { view ->
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.note_edit_image_menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete_image_menu_item -> deleteLoadedImage()
                }
                false
            }
            popupMenu.show()
            true
        }

    }

    private fun deleteLoadedImage(): Boolean {
        image.setImageResource(R.drawable.ic_baseline_add_a_photo_24)
        bitmapArray = byteArrayOf()
        binding.imageRotationButtonsContainer!!.visibility = View.GONE
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val returnUri: Uri = data!!.data!!

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, returnUri)
                image.setImageBitmap(bitmap)
                GlobalScope.launch(Dispatchers.Default) {
                    bitmapToByteArray(bitmap)
                }
                binding.imageRotationButtonsContainer!!.visibility = View.VISIBLE
            }
            catch (exception: RuntimeException) {
                Toast.makeText(
                    requireContext(), getString(R.string.image_loading_error_string), Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private suspend fun bitmapToByteArray(bitmap: Bitmap) {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos)
        bitmapArray = bos.toByteArray()
        bos.close()
    }

    override fun onDestroy() {
        viewModel.removeLocationListener()
        super.onDestroy()
    }

    companion object {

        fun getInstance(id: String?): NoteEditFragment {
            val fragment = NoteEditFragment()
            val data = Bundle()
            data.putString(argsBundleKey, id)
            fragment.arguments = data
            return fragment
        }
    }

}