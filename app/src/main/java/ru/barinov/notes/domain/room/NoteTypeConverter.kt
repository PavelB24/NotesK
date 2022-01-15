package ru.barinov.notes.domain.room

import androidx.room.TypeConverter
import ru.barinov.notes.domain.models.NoteTypes

class NoteTypeConverter {
    @TypeConverter
    fun toType(value: String) = enumValueOf<NoteTypes>(value)

    @TypeConverter
    fun fromType(value: NoteTypes) = value.name
}