package ru.barinov.notes.ui.profileFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.barinov.R
import ru.barinov.databinding.ProfileLayoutBinding
import ru.barinov.notes.ui.notesActivity.NotesActivity
import java.util.regex.Pattern


class ProfileFragment: Fragment() {
    private lateinit var binding: ProfileLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as NotesActivity).bottomNavigationItemView.setBackgroundColor(resources.getColor(R.color.deep_blue_2))
        super.onViewCreated(view, savedInstanceState)
    }
    }
