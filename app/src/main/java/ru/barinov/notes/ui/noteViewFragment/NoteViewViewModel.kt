package ru.barinov.notes.ui.noteViewFragment

import android.util.Log
import ru.barinov.notes.domain.curentDataBase.NotesRepository


class NoteViewViewModel: NoteViewContract.NoteViewFragmentPresenterInterface {
    override var view: NoteViewContract.ViewInterface? = null
    override  var  repository: NotesRepository? = null
    override  var  id: String? = null


    override fun getNote() {
        var note = repository?.getById(id)
        Log.d("@@@", note.toString())
        view?.fillTheFields(note!!.title, note.detail, note.dateAsString)
    }



}