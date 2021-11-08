package ru.barinov.notes.domain

import android.util.Log

class Iterator {
    private val searchCache = ArrayList<NoteEntity>()
    private val selectedCache = ArrayList<NoteEntity>()




    fun searchNotes(query: String, allNotes: MutableList<NoteEntity>) {
        searchCache.clear()
        query.toLowerCase()
        val size = query.length
        for (note in allNotes) {
            val title = note.title.toLowerCase()
            if (size > title.length) {
                return
            }
            for (i in 0 until size) {
                if (query[i] != title[i]) {
                    break
                } else
                    if (i == size - 1) {
                        searchCache.add(note)
                    }
            }
        }
    }

    fun addSelectedNote(note: NoteEntity){
        selectedCache.add(note)
    }

    fun clearSearchCache(){
        searchCache.clear()
    }
    fun clearSelectedCache(){
        searchCache.clear()
    }

    fun isSearchCacheEmpty(): Boolean{
        return searchCache.isEmpty()
    }

    fun getSearchedNotes(): ArrayList<NoteEntity>{
        return searchCache
    }
    fun getChosenNotes(): ArrayList<NoteEntity> {
        return selectedCache
    }

    fun findInSelectedCache(id: String): Boolean{
        for (note in selectedCache) {
            if (note.id.equals(id)) {
                return true
            }
        }
        return false
    }
    fun removeNoteFromSelectedCache(id: String): Boolean {
        Log.d("@@@", "Зашёл")
        val range = 0 until selectedCache.size
        for (i in range) {
            if (selectedCache[i].id.equals(id)) {
                selectedCache.removeAt(i)
                return true
            }
        }
        return false
    }
}