package com.panjikrisnayasa.caripura.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.panjikrisnayasa.caripura.model.User

class SignUpViewModel() : ViewModel() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mUserDatabaseReference: DatabaseReference
    private val mCode = MutableLiveData<Int>()

    fun signUp(
        fullName: String,
        phoneNumber: String,
        email: String,
        password: String
    ): LiveData<Int> {
        mAuth = FirebaseAuth.getInstance()
        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference("users")

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val cUser = mAuth.currentUser
                if (cUser != null) {
                    val user = User(cUser.uid, fullName, phoneNumber, email, "contributor")
                    mUserDatabaseReference.child(cUser.uid).setValue(user)

                    cUser.sendEmailVerification().addOnCompleteListener { that ->
                        if (that.isSuccessful) {
                            mCode.postValue(1)
                            mAuth.signOut()
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