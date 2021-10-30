package ru.barinov.notes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment

class DataManagerFragment: Fragment() {









    companion object {
        fun getInstance(data: Bundle?): NoteEditFragment {
            val dataManagerInstance = NoteEditFragment()
            dataManagerInstance.arguments = data
            return dataManagerInstance
        }
    }
}


