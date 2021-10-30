package ru.barinov.notes.domain

import android.os.Parcel
import android.os.Parcelable

class NotesRepository() : RepositoryInterface, Parcelable {
    override val allNotes= ArrayList<NoteEntity>()
        get() = ArrayList(field)
    override val searchCache = ArrayList<NoteEntity>()

    constructor(parcel: Parcel) : this() {
        parcel.writeList(allNotes)
    }


    override fun addNote(note: NoteEntity) {
        allNotes.add(note)
    }

    override fun addAll(arrayList: List<NoteEntity>) {
        allNotes.addAll(arrayList)
    }

    override fun removeNote(id: String): Boolean {
        val range= 0..allNotes.size
        for(i in range){
            if(allNotes[i].id.equals(id)){
                allNotes.removeAt(i)
                return true
            }
        }
       return  false
    }

    override fun updateNote(id: String, note: NoteEntity): Boolean {
        removeNote(id)
        note.id=id
        allNotes.add(note)
        return true
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }



    companion object CREATOR : Parcelable.Creator<NotesRepository> {
        override fun createFromParcel(parcel: Parcel): NotesRepository {
            return NotesRepository(parcel)
        }

        override fun newArray(size: Int): Array<NotesRepository?> {
            return arrayOfNulls(size)
        }


    }
}