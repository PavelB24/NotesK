package ru.barinov.notes.domain

import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator


data class NoteEntity(
    var id: String,
    var title: String,
    var detail: String,
    var originDay: Int,
    var originMonth: Int,
    var originYear: Int
) : Parcelable {


    val dateAsString: String
        get() = "$originDay.$originMonth.$originYear"

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
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