package ru.barinov.notes.ui.profileFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.barinov.R
import ru.barinov.databinding.ProfileRegistrationLayoutBinding
import ru.barinov.notes.ui.Application
import ru.barinov.notes.ui.notesActivity.Activity

class RegistrationFragment: Fragment() {
    private lateinit var binding: ProfileRegistrationLayoutBinding
    private lateinit var submitButton: Button
    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileRegistrationLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as Activity).bottomNavigationItemView.setBackgroundColor(resources.getColor(
            R.color.deep_blue_2))
        submitButton= binding.profileLoginButton
        loginEditText =binding.profileLoginEdittext
        passwordEditText = binding.profilePasswordEdittext
        onButtonPressed()
        super.onViewCreated(view, savedInstanceState)
    }

   private fun onButtonPressed(){
        submitButton.setOnClickListener {
            (requireActivity().application as Application).authentication.auth
                .createUserWithEmailAndPassword(loginEditText.text.toString().trim(), passwordEditText.text.toString().trim())
                .addOnCompleteListener(requireActivity()) {task ->
                    if(task.isSuccessful){
                        Log.d("@@@", "createUserWithEmail:success")
                        Toast.makeText(context, "Profile created.",
                            Toast.LENGTH_SHORT).show()
                        val user = (requireActivity().application as Application).authentication.auth.currentUser
                        parentFragmentManager.popBackStackImmediate()
                    }
                    else{
                        Toast.makeText(context, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}