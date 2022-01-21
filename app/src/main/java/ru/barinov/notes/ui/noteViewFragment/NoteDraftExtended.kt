package ru.barinov.notes.ui.noteViewFragment

import ru.barinov.notes.domain.models.NoteTypes

data class NoteDraftExtended(
    val title: String,
    val content: String,
    val date: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val type: NoteTypes,
    val image: ByteArray
)