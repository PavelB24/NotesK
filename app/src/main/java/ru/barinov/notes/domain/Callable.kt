package ru.barinov.notes.domain

import android.os.Bundle

interface Callable {
    fun callEditionFragment(data: Bundle?)
    fun callSettingsFragment()
    fun callNoteViewFragment(data: Bundle?)
}
