package ru.barinov.notes.domain.interfaces

import android.view.View
import ru.barinov.notes.domain.models.NoteEntity

interface OnNoteClickListener {
    fun onClickEdit(note: NoteEntity)
    fun onClickDelete(note: NoteEntity)
    fun onNoteClick(note: NoteEntity)
    fun onNoteLongClick(note: NoteEntity, view: View)
    fun onNoteChecked(note: NoteEntity)
    fun onNoteUnChecked(note: NoteEntity)
    fun onFavButtonPressed(note: NoteEntity)
}