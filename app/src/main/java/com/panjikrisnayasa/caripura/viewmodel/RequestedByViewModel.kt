package com.panjikrisnayasa.caripura.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.panjikrisnayasa.caripura.model.User

class RequestedByViewModel : ViewModel() {

    private lateinit var mDatabaseReference: DatabaseReference
    private val mUser = MutableLiveData<User>()

    fun getUserData(contributorId: String): LiveData<User> {
        mDatabaseReference =
            FirebaseDatabase.getInstance().getReference("user").child(contributorId)
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
        return mUser
    }
}