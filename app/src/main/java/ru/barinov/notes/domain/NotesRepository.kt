package ru.barinov.notes.domain

import android.os.Parcel
import android.os.Parcelable

class NotesRepository : RepositoryInterface, Parcelable {
    override val allNotes = ArrayList<NoteEntity>()
        get() = ArrayList(field)
    override val searchCache = ArrayList<NoteEntity>()


    override fun addNote(note: NoteEntity) {
        allNotes.add(note)
    }

    override fun addAll(arrayList: List<NoteEntity>) {
        allNotes.addAll(arrayList)
    }

    override fun removeNote(id: String): Boolean {
        val range = 0..allNotes.size
        for (i in range) {
            if (allNotes[i].id.equals(id)) {
                allNotes.removeAt(i)
                return true
            }
        }
        return false
    }

    override fun updateNote(id: String, note: NoteEntity): Boolean {
        removeNote(id)
        note.id = id
        allNotes.add(note)
        return true
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(allNotes)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotesRepository> {
        override fun createFromParcel(parcel: Parcel): NotesRepository {
            return NotesRepository()
        }

        override fun newArray(size: Int): Array<NotesRepository?> {
            return arrayOfNulls(size)
        }
    }


}