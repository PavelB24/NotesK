package ru.barinov.notes.ui.noteViewFragment

import android.util.Log
import ru.barinov.notes.domain.NoteEntity
import ru.barinov.notes.ui.Application


class NoteViewFragmentPresenter: NoteViewFragmentContract.NoteViewFragmentPresenterInterface {
    private var view:  NoteViewFragment? = null
    private  var  note: NoteEntity? = null

    override fun onAttach(view: NoteViewFragment) {
        this.view=view
        note = (view.requireActivity().application as Application).repository.getById(getIdFromRouter())
        Log.d("@@@", (view.requireActivity().application as Application).router.getId()!!)
        (view.requireActivity().application as Application).router.resetId()

    }

    override fun onDetach() {
        view= null
        note= null
    }

    override fun getNote() {
        Log.d("@@@", note.toString())
        view?.fillTheFields(note!!.title, note!!.detail, note!!.dateAsString)
    }

    override fun onBackPressed() {
        view?.parentFragmentManager?.popBackStack()
    }

    override fun getIdFromRouter(): String?{
        return (view?.requireActivity()?.application as Application).router.getId()
    }
}