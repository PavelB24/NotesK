package ru.barinov.notes.domain

import android.view.View
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity

interface OnNoteClickListener {
    fun onClickEdit(note: NoteEntity?)
    fun onClickDelete(note: NoteEntity)
    fun onNoteClick(note: NoteEntity)
    fun onNoteLongClick(note: NoteEntity, view: View)
    fun onNoteChecked(note: NoteEntity)
    fun onNoteUnChecked(note: NoteEntity)
}