package com.panjikrisnayasa.caripura.viewmodel.guest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.panjikrisnayasa.caripura.model.Temple

class TempleListViewModel : ViewModel() {

    private lateinit var mDatabaseReference: DatabaseReference
    private val mTempleList = MutableLiveData<ArrayList<Temple>>()

    internal fun getTemple(): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("geo_fire")
        mDatabaseReference.addChildEventListener(object : ChildEventListener {

            override fun onCancelled(p0: DatabaseError) {
                Log.d("hyperLoop", p0.message)
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                if (p0.exists()) {
                    val temple = p0.child("data").getValue(Temple::class.java)
                    Log.d("hyperLoop", "temple id ${temple?.id}")
                    if (temple != null) {
                        Log.d("hyperLoop", "view model temple not null")
                        templeList.add(temple)
                    }
                }
                mTempleList.postValue(templeList)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("Not yet implemented")
            }
        })
        return mTempleList
    }
}