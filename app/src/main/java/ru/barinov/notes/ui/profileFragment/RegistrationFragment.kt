package ru.barinov.notes.ui.profileFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ru.barinov.R
import ru.barinov.databinding.ProfileEnterLayoutBinding
import ru.barinov.databinding.ProfileRegistrationLayoutBinding
import ru.barinov.notes.ui.notesActivity.NotesActivity

class RegistrationFragment: Fragment() {
    private lateinit var binding: ProfileRegistrationLayoutBinding
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileRegistrationLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as NotesActivity).bottomNavigationItemView.setBackgroundColor(resources.getColor(
            R.color.deep_blue_2))
        submitButton= binding.profileLoginButton
        super.onViewCreated(view, savedInstanceState)
    }
}