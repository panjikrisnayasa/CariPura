package com.panjikrisnayasa.caripura.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoggedInViewModel : ViewModel() {

    private lateinit var mAuth: FirebaseAuth

    fun signOut() {
        mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()
    }
}