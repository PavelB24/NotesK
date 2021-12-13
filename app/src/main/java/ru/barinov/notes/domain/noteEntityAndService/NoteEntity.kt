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
    var originDay: Int,
    var originMonth: Int,
    var originYear: Int,
    var latitude: Double,
    var longitude: Double
) : Parcelable {
    constructor() : this("", "", "", -1, -1, -1, 0.0, 0.0 )
    val dateAsString: String
        get() = "$originDay.$originMonth.$originYear"

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble()
    )


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(detail)
        parcel.writeInt(originDay)
        parcel.writeInt(originMonth)
        parcel.writeInt(originYear)
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