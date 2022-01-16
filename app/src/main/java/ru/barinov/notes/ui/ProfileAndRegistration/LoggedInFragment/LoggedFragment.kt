package ru.barinov.notes.ui.ProfileAndRegistration.LoggedInFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.R

import ru.barinov.databinding.ProfileSingedLayoutBinding
import ru.barinov.notes.ui.ProfileAndRegistration.ProfileEnteringFragment.ProfileEnteringFragment

class LoggedFragment : Fragment() {

    private val viewModel by viewModel<LoggedFragmentViewModel>()
    private lateinit var binding: ProfileSingedLayoutBinding
    private lateinit var profileOutButton: Button
    private lateinit var profileNameTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ProfileSingedLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initViews()
        setListeners()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun setListeners() {
        profileOutButton.setOnClickListener {
            viewModel.onProfileOutButtonPressed()
        }
        viewModel.onUnsuccessfulProfileExit.observe(viewLifecycleOwner) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container_for_fragment, ProfileEnteringFragment()).commit()
            Toast.makeText(context, getString(R.string.on_log_out_string), Toast.LENGTH_SHORT).show()
        }
        viewModel.requireUserName()
        viewModel.userNameLiveData.observe(viewLifecycleOwner) { userName ->
            profileNameTextView.text = userName
        }
    }

    private fun initViews() {
        profileOutButton = binding.profileOutButton
        profileNameTextView = binding.profileNameTextView
    }

}