package ru.barinov.notes.ui.noteEditFragment

import ru.barinov.notes.domain.models.NoteTypes

data class NoteDraft(
    val title: String,
    val content: String,
    val type: NoteTypes,
    val image: ByteArray,
)

