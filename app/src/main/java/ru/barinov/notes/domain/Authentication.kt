package ru.barinov.notes.domain

import com.google.firebase.auth.ktx.auth

import com.google.firebase.ktx.Firebase


class Authentication {
   val auth = Firebase.auth
   var isOnline: Boolean= false
}
