package com.panjikrisnayasa.caripura.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoggedInViewModel : ViewModel() {

    fun signOut() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
    }
}