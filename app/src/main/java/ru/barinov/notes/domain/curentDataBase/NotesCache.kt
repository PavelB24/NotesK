package ru.barinov.notes.domain.curentDataBase

import android.util.Log
import ru.barinov.notes.domain.noteEntityAndService.NoteEntity
import java.util.*
import kotlin.collections.ArrayList

class NotesCache {
    private val searchCache = ArrayList<NoteEntity>()
    private val selectedCache = ArrayList<NoteEntity>()
    private val favCache = ArrayList<NoteEntity>()


    fun searchNotes(query: String, allNotes: MutableList<NoteEntity>) {
        searchCache.clear()
        query.lowercase(Locale.getDefault())
        var titleLetterIndex = 0
        var queryLetterIndex = 0
        val size = query.length

        for (note in allNotes) {
            while (titleLetterIndex < note.title.length) {
                if (note.title[titleLetterIndex] == query[queryLetterIndex]) {
                    if (queryLetterIndex == size - 1) {
                        searchCache.add(note)
                        break
                    }
                    titleLetterIndex++
                    queryLetterIndex++
                } else if (queryLetterIndex != 0) {
                    queryLetterIndex = 0
                } else {
                    titleLetterIndex++
                }
            }
            titleLetterIndex = 0
            queryLetterIndex = 0
        }
    }


    fun addSelectedNote(note: NoteEntity) {
        selectedCache.add(note)
    }

    fun clearSearchCache() {
        searchCache.clear()
    }

    fun clearSelectedCache() {
        searchCache.clear()
    }

    fun isSearchCacheEmpty(): Boolean {
        return searchCache.isEmpty()
    }

    fun getSearchedNotes(): ArrayList<NoteEntity> {
        return ArrayList(searchCache)
    }

    fun getChosenNotes(): ArrayList<NoteEntity> {
        return selectedCache
    }

    fun findInSelectedCache(id: String): Boolean {
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

    fun findAllFavNotes(allNotes: MutableList<NoteEntity>) {
        allNotes.forEach {note->
            if (note.isFavorite) {
                favCache.add(note)
            }
        }
    }

    fun getFavs(): ArrayList<NoteEntity> {
        return ArrayList(favCache)
    }

    fun clearFavCache() {
        favCache.clear()
    }

    fun removeFromFavs(note: NoteEntity) {
        favCache.remove(note)
    }

}