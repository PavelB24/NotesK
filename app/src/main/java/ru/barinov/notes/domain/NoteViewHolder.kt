package ru.barinov.notes.domain


import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ru.barinov.R

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val titleTextView: TextView = itemView.findViewById(R.id.note_title_textview)
    val descriptionTextView: TextView = itemView.findViewById(R.id.note_description_textview)
    val checkBox: CheckBox = itemView.findViewById(R.id.notes_checkbox)


}