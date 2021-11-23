package ru.barinov.notes.ui.profileFragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.barinov.R

import ru.barinov.databinding.ProfileSingedLayoutBinding
import ru.barinov.notes.ui.Application
import ru.barinov.notes.ui.notesActivity.Activity

class LoggedFragment: Fragment() {
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
        (requireActivity() as Activity).bottomNavigationItemView.setBackgroundColor(resources.getColor(
            R.color.deep_blue_2))
        binding.profileOutButton.setOnClickListener {
            singOut()
            Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT ).show()
        }
        binding.profileNameTextView.text = (requireActivity().application as Application).authentication.auth.currentUser?.email.toString()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun singOut() {
        (requireActivity().application as Application).authentication.isOnline = false
        (requireActivity().application as Application).authentication.auth.signOut()
        if(this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
        parentFragmentManager.beginTransaction().replace(R.id.layout_horizontal_unit_container,
            Profile()).commit()
        } else{
            parentFragmentManager.beginTransaction().replace(R.id.container_for_fragment,
                Profile()).commit()
        }
    }
}