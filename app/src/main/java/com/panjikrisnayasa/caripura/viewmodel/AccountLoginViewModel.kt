package com.panjikrisnayasa.caripura.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.panjikrisnayasa.caripura.model.User
import com.panjikrisnayasa.caripura.util.SingleLiveEvent

class AccountLoginViewModel : ViewModel() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabaseReference: DatabaseReference
    private val mUser = MutableLiveData<User>()
    private var mCode = SingleLiveEvent<Int>()

    fun authenticate(
        email: String,
        password: String
    ): LiveData<Int> {
        mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            val cUser = mAuth.currentUser
            if (it.isSuccessful) {
                if (cUser != null) {
                    val cUid = cUser.uid
                    mDatabaseReference =
                        FirebaseDatabase.getInstance().getReference("user").child(cUid)
                    mDatabaseReference.addValueEventListener(object : ValueEventListener {

                        override fun onCancelled(p0: DatabaseError) {
                            p0.message
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()) {
                                val user = p0.getValue(User::class.java)
                                if (user != null) {
                                    if (user.role == "contributor") {
                                        if (!cUser.isEmailVerified) {
                                            mCode.postValue(2)
                                            mAuth.signOut()
                                            return
                                        }
                                    }
                                    mCode.postValue(1)
                                }
                            }
                        }
                    })

                }
            } else {
                mCode.postValue(3)
            }
        }
        return mCode
    }

    fun getCurrentUser(): LiveData<User> {
        mAuth = FirebaseAuth.getInstance()
        val cUser = mAuth.currentUser
        if (cUser != null) {
            val cUid = cUser.uid
            mDatabaseReference = FirebaseDatabase.getInstance().getReference("user").child(cUid)
            mDatabaseReference.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    p0.message
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        val user = p0.getValue(User::class.java)
                        if (user != null)
                            mUser.postValue(user)
                    }
                }
            })
        }
        return mUser
    }
}