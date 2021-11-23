package ru.barinov.notes.ui.noteViewFragment

import android.util.Log
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity
import ru.barinov.notes.ui.Application
import ru.barinov.notes.ui.noteEditFragment.NoteEditFragmentContract


class NoteViewPresenter: NoteViewContract.NoteViewFragmentPresenterInterface {
    override var view: NoteViewContract.ViewInterface? = null
    override  var  repository: NotesRepository? = null
    override  var  id: String? = null

    override fun onAttach(view: NoteViewContract.ViewInterface, repository: NotesRepository, id: String) {
        this.view=view
        this.repository = repository
        this.id = id

    }

    override fun onDetach() {
        view= null
        repository= null
    }

    override fun getNote() {
        var note = repository?.getById(id)
        Log.d("@@@", note.toString())
        view?.fillTheFields(note!!.title, note.detail, note.dateAsString)
    }



}