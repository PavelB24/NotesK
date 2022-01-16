package ru.barinov.notes.ui.ProfileAndRegistration.ProfileEnteringFragment


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.R
import ru.barinov.databinding.ProfileEnterLayoutBinding
import ru.barinov.notes.ui.ProfileAndRegistration.AuthenticationDataDraft
import ru.barinov.notes.ui.ProfileAndRegistration.LoggedInFragment.LoggedFragment
import ru.barinov.notes.ui.ProfileAndRegistration.RegistrationFragment.RegistrationFragment


class ProfileEnteringFragment : Fragment() {

    private val viewModel by viewModel<ProfileEnteringFragmentViewModel>()
    private lateinit var binding: ProfileEnterLayoutBinding
    private lateinit var enterButton: Button
    private lateinit var registrationButton: Button
    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ProfileEnterLayoutBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initViews()
        restoreFields()
        setListeners()



        super.onViewCreated(view, savedInstanceState)
    }

    private fun setListeners() {
        registrationButton.setOnClickListener { startRegistrationFragment() }

        enterButton.setOnClickListener {
            viewModel.onEnterButtonPressed(
                AuthenticationDataDraft(loginEditText.text.toString(), passwordEditText.text.toString())
            )

            viewModel.onSuccessfulEnter.observe(viewLifecycleOwner) {
                singIn()
                Toast.makeText(context, R.string.on_success_auth_text, Toast.LENGTH_SHORT).show()
            }

            viewModel.onUnSuccessfulEnter.observe(viewLifecycleOwner) {
                Toast.makeText(context, R.string.on_fail_auth, Toast.LENGTH_SHORT).show()
            }

            viewModel.onOneOrMoreFieldsAreEmpty.observe(viewLifecycleOwner){
                Toast.makeText(context, getString(R.string.login_or_password_are_empty_string), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initViews() {
        enterButton = binding.profileLoginButton
        loginEditText = binding.profileLoginEdittext
        passwordEditText = binding.profilePasswordEdittext
        registrationButton = binding.profileCreateNewProfileButton
    }

    private fun restoreFields() {
        viewModel.restoreLastAuthenticationData()

        viewModel.authenticationDataRestored.observe(viewLifecycleOwner) { draft ->
            loginEditText.setText(draft.login)
            passwordEditText.setText(draft.password)
        }
    }




    private fun singIn() {
        parentFragmentManager.beginTransaction().replace(R.id.container_for_fragment, LoggedFragment())
            .commit()
    }

    private fun startRegistrationFragment() {
        parentFragmentManager.beginTransaction().add(R.id.container_for_fragment, RegistrationFragment())
            .addToBackStack(null).commit()
    }



}

