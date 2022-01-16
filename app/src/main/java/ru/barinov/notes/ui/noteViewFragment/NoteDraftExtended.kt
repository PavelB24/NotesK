package ru.barinov.notes.ui.noteViewFragment


data class NoteDraftExtended(
    val title: String,
    val content: String,
    val date: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val image: ByteArray
)