package com.panjikrisnayasa.caripura.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.panjikrisnayasa.caripura.model.Temple

class TempleRequestHistoryViewModel : ViewModel() {

    private lateinit var mDatabaseReference: DatabaseReference
    private val mTempleList = MutableLiveData<ArrayList<Temple>>()

    fun getAddTempleApprovalHistory(): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        mDatabaseReference =
            FirebaseDatabase.getInstance().getReference("history").child("add_request")
        mDatabaseReference.addChildEventListener(object : ChildEventListener {

            override fun onCancelled(p0: DatabaseError) {
                p0.message
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
                    if (temple != null) {
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

    fun getEditTempleApprovalHistory(): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        mDatabaseReference =
            FirebaseDatabase.getInstance().getReference("history").child("edit_request")
        mDatabaseReference.addChildEventListener(object : ChildEventListener {

            override fun onCancelled(p0: DatabaseError) {
                p0.message
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
                    if (temple != null) {
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

    fun getDeleteTempleApprovalHistory(): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        mDatabaseReference =
            FirebaseDatabase.getInstance().getReference("history").child("delete_request")
        mDatabaseReference.addChildEventListener(object : ChildEventListener {

            override fun onCancelled(p0: DatabaseError) {
                p0.message
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
                    if (temple != null) {
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

    fun getAddTempleRequestHistory(contributorId: String): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        mDatabaseReference =
            FirebaseDatabase.getInstance().getReference("history").child("add_request")
                .child(contributorId)
        mDatabaseReference.addChildEventListener(object : ChildEventListener {

            override fun onCancelled(p0: DatabaseError) {
                p0.message
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
                    if (temple != null) {
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

    fun getEditTempleRequestHistory(contributorId: String): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        mDatabaseReference =
            FirebaseDatabase.getInstance().getReference("history").child("edit_request")
                .child(contributorId)
        mDatabaseReference.addChildEventListener(object : ChildEventListener {

            override fun onCancelled(p0: DatabaseError) {
                p0.message
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
                    if (temple != null) {
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

    fun getDeleteTempleRequestHistory(contributorId: String): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        mDatabaseReference =
            FirebaseDatabase.getInstance().getReference("history").child("delete_request")
                .child(contributorId)
        mDatabaseReference.addChildEventListener(object : ChildEventListener {

            override fun onCancelled(p0: DatabaseError) {
                p0.message
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
                    if (temple != null) {
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