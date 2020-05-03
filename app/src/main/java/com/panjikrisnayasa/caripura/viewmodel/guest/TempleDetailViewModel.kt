package com.panjikrisnayasa.caripura.viewmodel.guest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.panjikrisnayasa.caripura.model.Temple

class TempleDetailViewModel : ViewModel() {

    private lateinit var mDatabaseReference: DatabaseReference
    private val mTempleDetail = MutableLiveData<Temple>()

    fun setTempleDetail(templeId: String) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("geo_fire")
        mDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val temple = p0.child(templeId).child("data").getValue(Temple::class.java)
                    Log.d("hyperLoop", "temple detail view model ${temple?.id}")
                    if (temple != null) {
                        mTempleDetail.postValue(temple)
                    }
                }
            }
        })
    }

    fun getTempleDetail(): LiveData<Temple> {
        return mTempleDetail
    }
}