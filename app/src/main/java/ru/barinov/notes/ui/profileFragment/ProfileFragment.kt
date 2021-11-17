package ru.barinov.notes.ui.profileFragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ru.barinov.R
import ru.barinov.databinding.ProfileEnterLayoutBinding
import ru.barinov.notes.ui.Application
import ru.barinov.notes.ui.application
import ru.barinov.notes.ui.notesActivity.NotesActivity
import java.util.regex.Pattern


class ProfileFragment: Fragment() {
    private lateinit var binding: ProfileEnterLayoutBinding
    private lateinit var enterButton: Button
    private lateinit var registrationButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileEnterLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as NotesActivity).bottomNavigationItemView.setBackgroundColor(resources.getColor(R.color.deep_blue_2))
        enterButton = binding.profileLoginButton
        registrationButton= binding.profileCreateNewProfileButton
        registrationButton.setOnClickListener {startRegistrationFragment()}
        super.onViewCreated(view, savedInstanceState)
    }

    private fun startRegistrationFragment(){
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            parentFragmentManager.beginTransaction().add(R.id.horizontal_unit_container, RegistrationFragment())
                .addToBackStack(RegistrationFragment::class.simpleName).commit()}
        else{
            parentFragmentManager.beginTransaction().add(R.id.container_for_fragment, RegistrationFragment())
                .addToBackStack(RegistrationFragment::class.simpleName).commit()}
        }
        }

