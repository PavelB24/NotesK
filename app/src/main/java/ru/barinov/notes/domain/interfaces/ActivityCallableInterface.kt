package ru.barinov.notes.domain.interfaces

import android.os.Bundle

interface ActivityCallableInterface {
    fun callEditionFragment(noteId: String?)
    fun callNoteViewFragment(noteId: String)
}
