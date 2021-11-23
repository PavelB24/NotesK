package ru.barinov.notes.ui.profileFragment

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.barinov.R
import ru.barinov.databinding.ProfileEnterLayoutBinding
import ru.barinov.notes.ui.Application
import ru.barinov.notes.ui.notesActivity.Activity


class Profile : Fragment() {
    private lateinit var binding: ProfileEnterLayoutBinding
    private lateinit var enterButton: Button
    private lateinit var registrationButton: Button
    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pref= requireActivity().getSharedPreferences("ProfileData", Context.MODE_PRIVATE)
        editor= pref.edit()
        binding = ProfileEnterLayoutBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as Activity).bottomNavigationItemView.setBackgroundColor(
            resources.getColor(
                R.color.deep_blue_2
            )
        )
        enterButton = binding.profileLoginButton
        loginEditText = binding.profileLoginEdittext
        passwordEditText = binding.profilePasswordEdittext
        registrationButton = binding.profileCreateNewProfileButton
        registrationButton.setOnClickListener { startRegistrationFragment() }
        onRestoreFields()
        onButtonPressed()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun onRestoreFields() {
        loginEditText.setText(pref.getString("Login", ""))
        passwordEditText.setText(pref.getString("Password", ""))
    }

    override fun onStart() {
        onRestoreFields()
        super.onStart()
    }

    private fun onButtonPressed() {
        enterButton.setOnClickListener {
            saveFields()
            (requireActivity().application as Application).authentication.auth
                .signInWithEmailAndPassword(
                    loginEditText.text.toString(),
                    passwordEditText.text.toString()
                )
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        (requireActivity().application as Application).authentication.isOnline =
                            true
                        singIn()
                        Toast.makeText(
                            context, R.string.on_success_auth,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context, R.string.on_fail_auth,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }



    private fun saveFields() {
        editor.putString("Login", loginEditText.text.toString()).apply()
        editor.putString("Password", passwordEditText.text.toString()).apply()
    }

    private fun singIn() {
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            parentFragmentManager.beginTransaction().replace(
                R.id.layout_horizontal_unit_container,
                LoggedFragment()
            ).commit()
        } else {
            parentFragmentManager.beginTransaction().replace(
                R.id.container_for_fragment,
                LoggedFragment()
            ).commit()
        }
    }

    private fun startRegistrationFragment() {
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            parentFragmentManager.beginTransaction()
                .add(R.id.layout_horizontal_unit_container, RegistrationFragment())
                .addToBackStack(RegistrationFragment::class.simpleName).commit()
        } else {
            parentFragmentManager.beginTransaction()
                .add(R.id.container_for_fragment, RegistrationFragment())
                .addToBackStack(RegistrationFragment::class.simpleName).commit()
        }
    }

}

