package ru.barinov.notes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment

class DataManagerFragment: Fragment() {









    companion object {
        fun getInstance(data: Bundle?): DataManagerFragment {
            val dataManagerInstance = DataManagerFragment()
            dataManagerInstance.arguments = data
            return dataManagerInstance
        }
    }
}


