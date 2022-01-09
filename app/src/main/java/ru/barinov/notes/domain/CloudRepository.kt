package ru.barinov.notes.domain

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ru.barinov.notes.domain.entity.NoteEntity

class CloudRepository {

    val auth = Firebase.auth
    val cloud = Firebase.firestore

    fun rewriteInCloud(note: NoteEntity) {
        if (auth.currentUser != null) {
            cloud.collection(
                auth.currentUser?.uid.toString()
            ).document(note.id).delete()
            cloud.collection(
                auth.currentUser?.uid.toString()
            ).document(note.id).set(note)
        }
    }

    fun deleteNoteInCloud(note: NoteEntity) {
        if (auth.currentUser != null) cloud.collection(
            auth.currentUser?.uid.toString()
        ).document(note.id).delete()
    }
}
