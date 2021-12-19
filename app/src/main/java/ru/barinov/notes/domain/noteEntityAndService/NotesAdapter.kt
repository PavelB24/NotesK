package ru.barinov.notes.domain.noteEntityAndService

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.barinov.R
import ru.barinov.notes.domain.DiffCallback
import ru.barinov.notes.domain.OnNoteClickListener
import java.util.ArrayList

class NotesAdapter: RecyclerView.Adapter<NoteViewHolder>() {
    var data: MutableList<NoteEntity> = ArrayList()
        set(newData){
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
        setTextInHolderItems(holder, note)
        setHolderItemsListeners(holder, note)
        holder.checkBox.isChecked = false

    }


    private fun setHolderItemsListeners(holder: NoteViewHolder, note: NoteEntity) {
        holder.itemView.setOnClickListener { listener.onNoteClick(note) }
        holder.itemView.setOnLongClickListener { listener.onNoteLongClick(note, it);true }
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
            listener.onNoteChecked(note)}
            else {
                listener.onNoteUnChecked(note)
            }
        }
    }

    private fun setTextInHolderItems(holder: NoteViewHolder, note: NoteEntity) {
        holder.titleTextView.text = note.title
        holder.descriptionTextView.text = note.detail
        holder.creationDateTextView.text=note.creationDate
    }

    override fun getItemCount(): Int {
        return data.size
    }
    private fun getNote(position: Int): NoteEntity {
        return data[position]
    }
    fun setListener(listener: OnNoteClickListener){
        this.listener= listener


    }


}