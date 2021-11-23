package ru.barinov.notes.domain

import android.content.Context
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CloudRepository {
    val cloud = Firebase.firestore
}
