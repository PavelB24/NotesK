package ru.barinov.notes.domain

import android.os.Parcel
import android.os.Parcelable
import android.util.Log

class NotesRepository : RepositoryInterface, Parcelable {
    override val allNotes: MutableList<NoteEntity> = ArrayList()

    override val searchCache = ArrayList<NoteEntity>()


    override fun addNote(note: NoteEntity) {
        Log.d("@@@", note.toString() +1)
        allNotes.add(note)
        Log.d("@@@", allNotes.toString() + 2)
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

    fun findById(id: String): Boolean {
        for(note in allNotes){
            if(note.id.equals(id)){
                return true
            }
        }
        return false
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