package ru.barinov.notes.ui.profileFragment

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.barinov.R
import ru.barinov.databinding.ProfileEnterLayoutBinding
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity
import ru.barinov.notes.ui.Application
import ru.barinov.notes.ui.application
import ru.barinov.notes.ui.dataManagerFragment.DataManager
import ru.barinov.notes.ui.notesActivity.Activity


class Profile : Fragment() {
    private lateinit var binding: ProfileEnterLayoutBinding
    private lateinit var enterButton: Button
    private lateinit var registrationButton: Button
    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var repository: NotesRepository
    private lateinit var cloudDataBase: CloudRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pref= requireActivity().getSharedPreferences("ProfileData", Context.MODE_PRIVATE)
        editor= pref.edit()
        binding = ProfileEnterLayoutBinding.inflate(inflater, container, false)
        requireActivity().window.statusBarColor= activity?.resources!!.getColor(R.color.deep_blue_2)
        requireActivity().window.navigationBarColor= activity?.resources!!.getColor(R.color.deep_blue_3)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as Activity).bottomAppBar.backgroundTint = ContextCompat.getColorStateList(requireContext(), R.color.deep_blue_3)
        enterButton = binding.profileLoginButton
        loginEditText = binding.profileLoginEdittext
        passwordEditText = binding.profilePasswordEdittext
        registrationButton = binding.profileCreateNewProfileButton
        registrationButton.setOnClickListener { startRegistrationFragment() }
        repository= requireActivity().application().repository
        cloudDataBase = requireActivity().application().cloudDataBase
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
                        synchronizeRepos()
                        singIn()
                        Toast.makeText(
                            context, R.string.on_success_auth_text,
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

    private fun synchronizeRepos() {
        if(requireActivity().application().authentication.auth.currentUser!=null) {
            val authentication = requireActivity().application().authentication
            Thread {
                cloudDataBase.cloud.collection(
                    authentication.auth.currentUser?.uid.toString()
                ).get().addOnSuccessListener { result ->
                    Log.d("@@@", "синхронизирует")
                    for (document in result) {
                        val note = document.toObject(NoteEntity::class.java)
                        if (!repository.findById(note.id)) {
                            repository.addNote(note)
                        }
                    }
                }
            }.start()
        }
    }


    private fun saveFields() {
        editor.putString("Login", loginEditText.text.toString()).apply()
        editor.putString("Password", passwordEditText.text.toString()).apply()
    }

    private fun singIn() {
        parentFragmentManager.setFragmentResult(
            DataManager::class.simpleName!!,
            Bundle().also { it.putBoolean(DataManager::class.simpleName!!, true) })
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

