package com.panjikrisnayasa.caripura.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.panjikrisnayasa.caripura.model.User

class SignUpViewModel : ViewModel() {

    private var mCode = MutableLiveData<Int>()

    fun signUp(
        fullName: String,
        phoneNumber: String,
        email: String,
        password: String
    ): LiveData<Int> {
        val auth = FirebaseAuth.getInstance()
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("user")

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val cUser = auth.currentUser
                if (cUser != null) {
                    val user = User(cUser.uid, fullName, phoneNumber, email, "contributor")
                    databaseReference.child(cUser.uid).setValue(user)

                    cUser.sendEmailVerification().addOnCompleteListener { that ->
                        if (that.isSuccessful) {
                            mCode.postValue(1)
                            auth.signOut()
                        } else {
                            mCode.postValue(2)
                        }
                    }
                } else {
                    mCode.postValue(3)
                }
            }
        }
        return mCode
    }
}