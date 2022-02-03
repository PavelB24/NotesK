package ru.barinov.notes.domain.adapters

import android.graphics.*
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.barinov.R
import ru.barinov.notes.domain.interfaces.OnNoteClickListener
import ru.barinov.notes.domain.models.NoteEntity
import java.text.SimpleDateFormat
import java.util.*
import android.os.Looper
import kotlinx.coroutines.*
import ru.barinov.notes.core.*

private const val imageScaleBase = 70

@DelicateCoroutinesApi
class NotesAdapter(
    private val scale: Float
) : RecyclerView.Adapter<NoteViewHolder>() {

    var data: List<NoteEntity> = ArrayList()
        set(newData) {
            val result = DiffUtil.calculateDiff(DiffCallback(data, newData), true)
            field = newData
            result.dispatchUpdatesTo(this)
        }
    private lateinit var listener: OnNoteClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val note = getNote(position)

        setIdleStateInHolderItems(holder, note)
        setHolderItemsListeners(holder, note)

        holder.checkBox.isChecked = false

        if (note.image.isNotEmpty()) {
            holder.noteImage.visibility = View.VISIBLE
            GlobalScope.launch(Dispatchers.Default) {
                val bitmap = BitmapFactory.decodeByteArray(note.image, 0, note.image.size)
                val scaledImgBitmap = bitmap.scaledImageFromBitmap(bitmap, imageScaleBase, scale)
                withContext(Dispatchers.Main) {
                    holder.noteImage.setImageBitmap(scaledImgBitmap)
                }
            }
        } else {
            holder.noteImage.visibility = View.GONE
        }

    }

    private fun setHolderItemsListeners(holder: NoteViewHolder, note: NoteEntity) {
        holder.itemView.setOnClickListener { listener.onNoteClick(note) }
        holder.itemView.setOnLongClickListener { view -> listener.onNoteLongClick(note, view);true }
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                listener.onNoteChecked(note)
            } else {
                listener.onNoteUnChecked(note)
            }
        }
        holder.favImgButton.setOnClickListener {
            listener.onFavButtonPressed(note)
            refreshIcon(note.isFavorite, holder.favImgButton)
        }
    }

    private fun refreshIcon(isFavorite: Boolean, favButton: ImageButton) {
        if (isFavorite) {
            favButton.setImageResource(R.drawable.ic_favourites_selected_star)
        } else {
            favButton.setImageResource(R.drawable.ic_favourites_black_star)
        }
    }

    private fun setIdleStateInHolderItems(holder: NoteViewHolder, note: NoteEntity) {
        holder.titleTextView.text = note.title
        holder.creationDateTextView.text = convertTimeInFormattedString(note)
        if (note.isFavorite) {
            holder.favImgButton.setImageResource(R.drawable.ic_favourites_selected_star)
        } else {
            holder.favImgButton.setImageResource(R.drawable.ic_favourites_black_star)
        }
    }

    private fun convertTimeInFormattedString(note: NoteEntity): String {
        val dateFormat = SimpleDateFormat("dd/M/yyyy")
        val date = Date()
        date.time = note.creationTime
        return dateFormat.format(date)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun getNote(position: Int): NoteEntity {
        return data[position]
    }

    fun setListener(listener: OnNoteClickListener) {
        this.listener = listener

    }

}