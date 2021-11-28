package ru.barinov.notes.domain

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.barinov.R
import java.util.ArrayList

class NotesAdapter: RecyclerView.Adapter<NoteViewHolder>() {
    var data: MutableList<NoteEntity> = ArrayList()
        set(newData){
            val result = DiffUtil.calculateDiff(DiffCallback(data, newData), true)
            field = newData
            result.dispatchUpdatesTo(this)
        }
    private lateinit var listener:OnNoteClickListener



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        var note = getNote(position)
        setTextInHolderItems(holder, note)
        setHolderItemsListeners(holder, note)
    }


    private fun setHolderItemsListeners(holder: NoteViewHolder, note: NoteEntity) {
        holder.itemView.setOnClickListener { listener.onNoteClick(note) }
        holder.itemView.setOnLongClickListener { listener.onNoteLongClick(note, it); true }
    }

    private fun setTextInHolderItems(holder: NoteViewHolder, note: NoteEntity) {
        holder.titleTextView.text = note.title
        holder.descriptionTextView.text = note.detail
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