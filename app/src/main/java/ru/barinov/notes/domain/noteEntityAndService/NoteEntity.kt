package ru.barinov.notes.domain.noteEntityAndService

import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_database")
data class NoteEntity(
    @PrimaryKey
    var id: String,
    var title: String,
    var detail: String,
    var latitude: Double,
    var longitude: Double,
    var creationDate: String,
) : Parcelable {
    constructor() : this("", "", "",- 0.0, 0.0, " " )



    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()!!
    )


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(detail)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(creationDate)

    }

    companion object CREATOR : Creator<NoteEntity> {
        override fun createFromParcel(parcel: Parcel): NoteEntity {
            return NoteEntity(parcel)
        }

        override fun newArray(size: Int): Array<NoteEntity?> {
            return arrayOfNulls(size)
        }
    }


}