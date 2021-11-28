package ru.barinov.notes.domain


import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.barinov.R

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var titleTextView = itemView.findViewById<TextView>(R.id.note_title_textview)
    var descriptionTextView = itemView.findViewById<TextView>(R.id.note_description_textview)


}