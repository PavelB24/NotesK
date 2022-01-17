package ru.barinov.notes.ui.dialogs

import android.graphics.BitmapFactory
import android.os.*
import android.view.*
import androidx.fragment.app.*
import com.google.android.material.imageview.ShapeableImageView
import ru.barinov.R
import ru.barinov.databinding.*
import java.lang.IllegalStateException

private const val argumentsKey = "argumentsKey"

class ImageFragment: DialogFragment() {

    private lateinit var binding: ImageDetailDialogFragmentLayoutBinding
    private lateinit var image: ShapeableImageView
    private val handler = Handler(Looper.getMainLooper())
    private val bitmapArray:ByteArray by lazy {
        val id = requireArguments().getByteArray(argumentsKey)
        if (id == null) {
            throw IllegalStateException("ID should be provided")
        }
        return@lazy id
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ImageDetailDialogFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        image = binding.imgDetailImageView
        Thread {
            val bitmapArray = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
            handler.post { image.setImageBitmap(bitmapArray) }
        }.start()
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {

        fun getInstance(imageAsByteArray: ByteArray): DialogFragment {
            val fragment = ImageFragment()
            val bundle = Bundle()
            bundle.putByteArray(argumentsKey, imageAsByteArray)
            fragment.arguments = bundle
            return fragment
        }
    }
}