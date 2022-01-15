package ru.barinov.notes.domain.models

import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator
import androidx.room.*
import ru.barinov.notes.domain.room.NoteTypeConverter

@Entity(tableName = "note_table")
data class NoteEntity(
    @PrimaryKey
    var id: String,
    var title: String,
    var content: String,
    var latitude: Double,
    var longitude: Double,
    var creationTime: Long,
    var isFavorite: Boolean,
    @ColumnInfo(name = "type_of_note")
    @TypeConverters(NoteTypeConverter::class)
    var type: NoteTypes,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray
) {
    constructor() : this("", "", "", -0.0, 0.0, 0L, false, NoteTypes.Idle, byteArrayOf())

}