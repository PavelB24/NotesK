package ru.barinov.notes.ui.noteViewFragment

import android.util.Log
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity
import ru.barinov.notes.ui.Application


class NoteViewPresenter: NoteViewContract.NoteViewFragmentPresenterInterface {
    private var view:  NoteView? = null
    private  var  note: NoteEntity? = null

    override fun onAttach(view: NoteView) {
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