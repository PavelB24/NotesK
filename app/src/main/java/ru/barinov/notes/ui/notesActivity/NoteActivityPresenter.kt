package ru.barinov.notes.ui.notesActivity

import android.content.Context.MODE_PRIVATE
import android.content.res.Configuration
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ru.barinov.R
import ru.barinov.notes.domain.NoteEntity
import ru.barinov.notes.domain.NotesRepository
import ru.barinov.notes.ui.Application
import ru.barinov.notes.ui.dataManagerFragment.DataManagerFragment
import ru.barinov.notes.ui.noteEditFragment.NoteEditFragment
import ru.barinov.notes.ui.noteListFragment.NoteListFragment
import ru.barinov.notes.ui.noteViewFragment.NoteViewFragment
import java.io.File
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.ArrayList

class NoteActivityPresenter: NotesActivityContract.NoteActivityPresenterInterface {
    private  var view:  NotesActivity? = null
    private lateinit var  repository: NotesRepository
    private val LOCAL_REPOSITORY_NAME = "repository.bin"
    private lateinit var  fragmentManager: FragmentManager



    override fun onAttach(view: NotesActivity) {
        this.view = view
        fragmentManager= view.supportFragmentManager
        repository = (view.application as Application).repository
    }

    override fun onDetach() {
        view= null
    }

    @Throws(IOException::class)
    override fun onSafeNotes() {
        val fos = view?.openFileOutput(LOCAL_REPOSITORY_NAME, MODE_PRIVATE)
        val objectOutputStream = ObjectOutputStream(fos)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(NoteEntity::class.java)
        objectOutputStream.writeInt(repository.allNotes.size)
        for (note in repository.allNotes) {
            val json = jsonAdapter.toJson(note)
            objectOutputStream.writeObject(json)
        }
        objectOutputStream.close()
        fos?.close()
        Log.d("@@@", "Записан")
    }


    override fun onReadNotes() {
        if(repository.allNotes.isEmpty()&& File(view?.filesDir, LOCAL_REPOSITORY_NAME).exists()){
            toInitNotesInRepository()
        }
    }

    override fun onChoseNavigationItem() {
        TODO("Not yet implemented")
    }

    override fun toInitNotesInRepository() {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(NoteEntity::class.java)
        val fileInputStream = view?.openFileInput(LOCAL_REPOSITORY_NAME)
        val objectInputStream = ObjectInputStream(fileInputStream)
        val size = objectInputStream.readInt()
        val list: MutableList<NoteEntity> = ArrayList()
        for (i in 0 until size) {
            val json: String = objectInputStream.readObject() as String
            list.add(jsonAdapter.fromJson(json) as NoteEntity)
        }
        repository.addAll(list)
        objectInputStream.close()
        fileInputStream!!.close()
    }

    override fun setNavigationListeners(bottomNavigationItemView: BottomNavigationView) {
        bottomNavigationItemView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.notes_item_menu -> {
                    if (view!!.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        fragmentManager.beginTransaction()
                            .replace(R.id.container_for_fragment_land_1, NoteListFragment())
                            .commit()
                    } else {
                        fragmentManager.beginTransaction().replace(
                            R.id.container_for_fragment,
                            NoteListFragment()
                        )
                            .commit()
                    }
                    fragmentManager.popBackStack()
                }
                R.id.data_manager_item_menu -> {
                    if (view!!.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        fragmentManager.beginTransaction()
                            .replace(
                                R.id.container_for_fragment_land_1,
                                DataManagerFragment()
                            )
                            .commit()
                    } else {
                        fragmentManager.beginTransaction()
                            .replace(
                                R.id.container_for_fragment,
                                DataManagerFragment()
                            )
                            .commit()
                    }
                }
            }; true
        }
        bottomNavigationItemView.selectedItemId= R.id.notes_item_menu
    }

    override fun editNote() {
        fragmentManager.popBackStack()
        if (view!!.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager.beginTransaction()
                .add(R.id.container_for_fragment_land_2, NoteEditFragment())
                .addToBackStack(null).commit()
        } else {
            fragmentManager.beginTransaction()
                .add(R.id.container_for_fragment, NoteEditFragment())
                .addToBackStack(null).commit()
        }
    }

    override fun openNote() {
        if (view!!.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager.beginTransaction()
                .add(R.id.container_for_fragment_land_2, NoteViewFragment())
                .addToBackStack(null).commit()
        } else {
            fragmentManager.beginTransaction()
                .add(R.id.container_for_fragment, NoteViewFragment())
                .addToBackStack(null).commit()
        }
    }
}