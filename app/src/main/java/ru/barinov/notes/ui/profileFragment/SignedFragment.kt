package ru.barinov.notes.ui.profileFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.barinov.R

import ru.barinov.databinding.ProfileSingedLayoutBinding
import ru.barinov.notes.ui.notesActivity.NotesActivity

class SignedFragment: Fragment() {
    private lateinit var binding: ProfileSingedLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileSingedLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as NotesActivity).bottomNavigationItemView.setBackgroundColor(resources.getColor(
            R.color.deep_blue_2))
        super.onViewCreated(view, savedInstanceState)
    }
}