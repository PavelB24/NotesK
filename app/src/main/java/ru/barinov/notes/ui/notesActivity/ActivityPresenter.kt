package ru.barinov.notes.ui.notesActivity

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.barinov.R
import ru.barinov.notes.domain.curentDataBase.NotesRepository
import ru.barinov.notes.domain.Router
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity
import ru.barinov.notes.ui.Application
import ru.barinov.notes.ui.application
import ru.barinov.notes.ui.dataManagerFragment.DataManager
import java.io.IOException

class ActivityPresenter : ActivityContract.NoteActivityPresenterInterface {
    private var view: Activity? = null
    private lateinit var repository: NotesRepository
    private val LOCAL_REPOSITORY_NAME = "repository.bin"
    private lateinit var fragmentManager: FragmentManager
    private lateinit var router: Router



    override fun onAttach(view: Activity) {
        this.view = view
        fragmentManager = view.supportFragmentManager
        repository = (view.application as Application).repository
        router = (view.application as Application).router
    }

    override fun onDetach() {
        view = null
    }

    @Throws(IOException::class)
    override fun safeNotes() {
//        val fos = view?.openFileOutput(LOCAL_REPOSITORY_NAME, MODE_PRIVATE)
//        val objectOutputStream = ObjectOutputStream(fos)
//        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//        val jsonAdapter = moshi.adapter(NoteEntity::class.java)
//        objectOutputStream.writeInt(repository.allNotes.size)
//        for (note in repository.allNotes) {
//            val json = jsonAdapter.toJson(note)
//            objectOutputStream.writeObject(json)
//        }
//        objectOutputStream.close()
//        fos?.close()
//        Log.d("@@@", "Записан")
    }


    override fun readNotes() {
        if (repository.allNotes.isEmpty()) {
            toInitNotesInRepository()
            fragmentManager.setFragmentResultListener( DataManager::class.simpleName!!, view!!, FragmentResultListener { requestKey, result ->
                if(result.getBoolean(DataManager::class.simpleName!!)){
                    toInitNotesFromCloud()
                }
            })

        }
    }

    override fun onChoseNavigationItem() {
        TODO("Not yet implemented")
    }

    override fun toInitNotesInRepository() {
        (view!!.application as Application).repository.addAll((view!!.application as Application).localDataBase.noteDao().getAllNotes())


        //Старый метод, шпаргалка по Moshi
//        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//        val jsonAdapter = moshi.adapter(NoteEntity::class.java)
//        val fileInputStream = view?.openFileInput(LOCAL_REPOSITORY_NAME)
//        val objectInputStream = ObjectInputStream(fileInputStream)
//        val size = objectInputStream.readInt()
//        val list: MutableList<NoteEntity> = ArrayList()
//        for (i in 0 until size) {
//            val json: String = objectInputStream.readObject() as String
//            list.add(jsonAdapter.fromJson(json) as NoteEntity)
//        }
//        repository.addAll(list)
//        objectInputStream.close()
//        fileInputStream!!.close()
    }


    override fun toInitNotesFromCloud() {
        if (view!!.application().authentication.auth.currentUser != null) {
            Thread {
                view!!.application().cloudDataBase.cloud.collection(view!!.application().authentication.auth.currentUser?.uid.toString())
                    .get().addOnSuccessListener { result ->
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
    override fun setNavigationListeners(bottomNavigationItemView: BottomNavigationView) {
        bottomNavigationItemView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.notes_item_menu -> {
                    openNoteList()
                }
                R.id.data_manager_item_menu -> {
                    openDataManager()
                }
                R.id.profile_item_menu -> {
                    openProfile()
                }
            }; true
        }
        setStartFragment(bottomNavigationItemView)
    }

    private fun setStartFragment(bottomNavigationItemView: BottomNavigationView) {
        if (view!!.application().authentication.auth.currentUser!=null) {
            bottomNavigationItemView.selectedItemId = R.id.notes_item_menu
        } else {
            bottomNavigationItemView.selectedItemId = R.id.profile_item_menu
        }
    }

    private fun openProfile() {
        router.openProfileFragment(getOrientation(), fragmentManager, (view?.application as Application).authentication.auth.currentUser!=null)
    }

    private fun openDataManager() {
        router.openDataManagerFragment(getOrientation(), fragmentManager)
    }


    private fun openNoteList() {
       router.openNoteLitFragment(getOrientation(), fragmentManager)
    }

    override fun editNote() {
           router.openNoteEditFragment(getOrientation(), fragmentManager)
    }

    override fun openNote() {
        router.openNoteViewFragment(getOrientation(), fragmentManager)
    }

    private fun getOrientation(): Int{
        return view!!.resources.configuration.orientation
    }

    fun logOut() {
        Thread{
        view!!.application().authentication.auth.signOut()
        }.start()
        view!!.application().authentication.isOnline = false
    }
}