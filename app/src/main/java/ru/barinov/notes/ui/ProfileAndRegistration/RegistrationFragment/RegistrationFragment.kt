package ru.barinov.notes.ui.ProfileAndRegistration.RegistrationFragment

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
import ru.barinov.databinding.ProfileRegistrationLayoutBinding
import ru.barinov.notes.ui.ProfileAndRegistration.AuthenticationDataDraft

class RegistrationFragment : Fragment() {

    private val viewModel by viewModel<RegistrationFragmentViewModel>()
    private lateinit var binding: ProfileRegistrationLayoutBinding
    private lateinit var submitButton: Button
    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ProfileRegistrationLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initViews()
        setListeners()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setListeners() {
        submitButton.setOnClickListener {
            val draft = AuthenticationDataDraft(
                loginEditText.text.toString(), passwordEditText.text.toString()
            )
            if (draft.login.isEmpty() || draft.password.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.registration_fields_are_empty), Toast.LENGTH_SHORT).show()

            } else {
                viewModel.registerNewUser(
                    draft)
            }
        }
        viewModel.onSuccessfulRegistration.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), getString(R.string.on_successful_registration_string), Toast.LENGTH_SHORT)
                .show()
            parentFragmentManager.popBackStackImmediate()
        }

        viewModel.onUnSuccessfulRegistration.observe(viewLifecycleOwner) {
            Toast.makeText(
                context, getString(R.string.on_unsuccessful_registration_string), Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initViews() {
        submitButton = binding.profileLoginButton
        loginEditText = binding.profileLoginEdittext
        passwordEditText = binding.profilePasswordEdittext
    }

}