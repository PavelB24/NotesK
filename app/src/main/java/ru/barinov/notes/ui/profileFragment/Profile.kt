package ru.barinov.notes.ui.profileFragment

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
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
import ru.barinov.databinding.ProfileEnterLayoutBinding
import ru.barinov.notes.domain.CloudRepository
import ru.barinov.notes.domain.userRepository.NotesRepository
import ru.barinov.notes.domain.entity.NoteEntity
import ru.barinov.notes.ui.Application
import ru.barinov.notes.ui.application
import ru.barinov.notes.ui.dataManagerFragment.DataManagerFragment

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
        savedInstanceState: Bundle?,
    ): View {
        pref = requireActivity().getSharedPreferences("ProfileData", Context.MODE_PRIVATE)
        editor = pref.edit()
        binding = ProfileEnterLayoutBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        enterButton = binding.profileLoginButton
        loginEditText = binding.profileLoginEdittext
        passwordEditText = binding.profilePasswordEdittext
        registrationButton = binding.profileCreateNewProfileButton
        registrationButton.setOnClickListener { startRegistrationFragment() }
        repository = requireActivity().application().repository
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
            (requireActivity().application as Application).cloudDataBase.auth.signInWithEmailAndPassword(
                    loginEditText.text.toString(),
                    passwordEditText.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        synchronizeRepos()
                        singIn()
                        Toast.makeText(context, R.string.on_success_auth_text, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, R.string.on_fail_auth, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun synchronizeRepos() {
        if (requireActivity().application().cloudDataBase.auth.currentUser != null) {
            val authentication = requireActivity().application().cloudDataBase
            Thread {
                cloudDataBase.cloud.collection(authentication.auth.currentUser?.uid.toString())
                    .get()
                    .addOnSuccessListener { result ->
                        Log.d("@@@", "синхронизирует")
                        for (document in result) {
                            val note = document.toObject(NoteEntity::class.java)
                            if (!repository.findById(note.id)) {
                                repository.insertNote(note)
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
        parentFragmentManager.setFragmentResult(DataManagerFragment::class.simpleName!!,
            Bundle().also { it.putBoolean(DataManagerFragment::class.simpleName!!, true) })
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.layout_horizontal_unit_container, LoggedFragment())
                .commit()
        } else {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container_for_fragment, LoggedFragment())
                .commit()
        }
    }

    //todo вынести в роутер
    private fun startRegistrationFragment() {
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            parentFragmentManager.beginTransaction()
                .add(R.id.layout_horizontal_unit_container, RegistrationFragment())
                .addToBackStack(null)
                .commit()
        } else {
            parentFragmentManager.beginTransaction()
                .add(R.id.container_for_fragment, RegistrationFragment())
                .addToBackStack(null)
                .commit()
        }
    }

}

