package ru.barinov.notes.domain.noteEntityAndService


import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ru.barinov.R

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val titleTextView: TextView = itemView.findViewById(R.id.note_title_textview)
    val checkBox: CheckBox = itemView.findViewById(R.id.notes_checkbox)
    val creationDateTextView: TextView = itemView.findViewById(R.id.creation_date_location)
    val favImgButton: ImageButton = itemView.findViewById(R.id.favorite_img_button)


}