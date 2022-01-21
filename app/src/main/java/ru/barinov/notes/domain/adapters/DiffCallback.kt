package ru.barinov.notes.domain.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.barinov.notes.domain.models.NoteEntity

class DiffCallback(oldList: List<NoteEntity>, newList: List<NoteEntity>) : DiffUtil.Callback() {
    private var oldList: List<NoteEntity> = oldList
    private var newList: List<NoteEntity> = newList


    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].id.equals(oldList[oldItemPosition].id)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val newNote = newList[newItemPosition]
        val oldNote = oldList[oldItemPosition]
        return newNote.title.equals(oldNote.title) && newNote.content
            .equals(oldNote.content) && newNote.creationTime
            .equals(oldNote.creationTime) && newNote.isFavorite.equals(oldNote.isFavorite)
    }
}



