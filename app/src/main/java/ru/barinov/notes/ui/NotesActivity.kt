package ru.barinov.notes.ui

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ru.barinov.R
import ru.barinov.databinding.MainLayoutBinding
import ru.barinov.notes.domain.Callable
import ru.barinov.notes.domain.NoteEntity
import ru.barinov.notes.domain.NotesRepository
import java.io.*
import java.util.ArrayList


class NotesActivity : AppCompatActivity(), Callable {
    private lateinit var binding: MainLayoutBinding
    private var fragmentManager: FragmentManager = supportFragmentManager
    private lateinit var bottomNavigationItemView: BottomNavigationView
    private val repository: NotesRepository
        get() = (application as Application).repository
    private val LOCAL_REPOSITORY_NAME = "local_repository.bin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavigation()
        //todo переписать под 2 варианта хранения
        if(repository.allNotes.isEmpty()&&File(filesDir, LOCAL_REPOSITORY_NAME).exists()){
        toInitNotesInRepository()}
        bottomNavigationItemView.selectedItemId= R.id.notes_item_menu
    }

    private fun setNavigation() {
        //TODO
        bottomNavigationItemView = binding.navigationBar
        val savedData = Bundle()
        savedData.putParcelable(NotesRepository::class.simpleName, repository)
        if (!savedData.isEmpty){ Log.d("@@@","не пусто")}
        bottomNavigationItemView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.notes_item_menu -> {
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        fragmentManager.beginTransaction()
                            .replace(
                                R.id.container_for_fragment_land_1,
                                NoteListFragment.getInstance(savedData)
                            )
                            .commit()
                    } else {
                        fragmentManager.beginTransaction().replace(
                            R.id.container_for_fragment,
                            NoteListFragment.getInstance(savedData)
                        )
                            .commit()
                    }
                    fragmentManager.popBackStack()
                }
                R.id.data_manager_item_menu -> {
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        fragmentManager.beginTransaction()
                            .replace(R.id.container_for_fragment_land_1, DataManagerFragment.getInstance(savedData))
                            .commit()
                    } else {
                        fragmentManager.beginTransaction()
                            .replace(
                                R.id.container_for_fragment,
                                DataManagerFragment.getInstance(savedData)
                            )
                            .commit()
                    }
                }
            }; true
        }
    }

    override fun onPause() {
        serializeNotes()
        super.onPause()
    }

    override fun callEditionFragment(data: Bundle?) {
        fragmentManager.popBackStack()
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager.beginTransaction()
                .add(R.id.container_for_fragment_land_2, NoteEditFragment.getInstance(data))
                .addToBackStack(null).commit()
        } else {
            fragmentManager.beginTransaction()
                .add(R.id.container_for_fragment, NoteEditFragment.getInstance(data))
                .addToBackStack(null).commit()
        }
    }

    override fun callSettingsFragment() {
        TODO("Not yet implemented")
    }

    override fun callNoteViewFragment(data: Bundle) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager.beginTransaction()
                .add(R.id.container_for_fragment_land_2, NoteViewFragment.getInstance(data))
                .addToBackStack(null).commit()
        } else {
            fragmentManager.beginTransaction()
                .add(R.id.container_for_fragment, NoteViewFragment.getInstance(data))
                .addToBackStack(null).commit()
        }
    }
    @Throws(IOException::class)
    private fun serializeNotes() {
        val fos = openFileOutput(LOCAL_REPOSITORY_NAME, MODE_PRIVATE)
        val objectOutputStream = ObjectOutputStream(fos)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(NoteEntity::class.java)
        objectOutputStream.writeInt(repository.allNotes.size)
        for (note in repository.allNotes) {
            var json = jsonAdapter.toJson(note)
            objectOutputStream.writeObject(json)
        }
        objectOutputStream.close()
        fos.close()
    }
    @Throws(IOException::class, ClassNotFoundException::class)
    private fun toInitNotesInRepository() {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(NoteEntity::class.java)
        val fileInputStream = openFileInput(LOCAL_REPOSITORY_NAME)
        val objectInputStream = ObjectInputStream(fileInputStream)
        val size = objectInputStream.readInt()
        val list: MutableList<NoteEntity> = ArrayList()
        for (i in 0 until size) {
            val json: String = objectInputStream.readObject() as String
            list.add(jsonAdapter.fromJson(json) as NoteEntity)
            Log.d("@@@", list.toString())
        }
        repository.addAll(list)
        Log.d("@@@", "size " + repository.allNotes.size)
        objectInputStream.close()
        fileInputStream.close()
        Log.d("@@@", "Восстановлен")
    }
}