package ru.barinov.notes.domain.curentDataBase

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity

class NotesRepository : RepositoryInterface, Parcelable {
    override val allNotes: MutableList<NoteEntity> = mutableListOf()

    fun deleteAll() {
        allNotes.clear()
    }




    override fun addNote(note: NoteEntity) {
        Log.d("@@@", note.toString() + 1)
        allNotes.add(note)
        Log.d("@@@", allNotes.toString() + 2)
    }

    override fun addAll(list: MutableList<NoteEntity>) {
        allNotes.addAll(list)
    }

    override fun removeNote(id: String): Boolean {
        val range = 0 until allNotes.size
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
        this.addNote(note)
        return true
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(allNotes)

    }

    override fun describeContents(): Int {
        return 0
    }

    fun findById(id: String): Boolean {
        for (note in allNotes) {
            if (note.id.equals(id)) {
                return true
            }
        }
        return false
    }

    fun getById(id: String?): NoteEntity? {
        for (note in allNotes) {
            if (note.id.equals(id)) {
                return note
            }
        }
        return null
    }

    fun getNotes(): MutableList<NoteEntity> {
        return ArrayList<NoteEntity>(allNotes)

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