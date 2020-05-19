package com.panjikrisnayasa.caripura.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.panjikrisnayasa.caripura.model.Temple

class MyTempleListViewModel : ViewModel() {

    private lateinit var mDatabaseReference: DatabaseReference
    private val mTempleList = MutableLiveData<ArrayList<Temple>>()

    fun getAdminTempleList(): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        mDatabaseReference =
            FirebaseDatabase.getInstance().getReference("temple").child("admin_temple")
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
                    val temple = p0.getValue(Temple::class.java)
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

    fun getTempleListApproved(contributorId: String): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        mDatabaseReference =
            FirebaseDatabase.getInstance().getReference("temple").child("contributor_temple")
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
                    if (p0.key == contributorId) {
                        val contributorTemple = p0.children
                        for (data in contributorTemple) {
                            val temple = data.getValue(Temple::class.java)
                            if (temple != null)
                                templeList.add(temple)
                        }
                    } else {
                        mTempleList.postValue(null)
                        return
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

    fun getTempleListWaiting(contributorId: String): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        mDatabaseReference =
            FirebaseDatabase.getInstance().getReference("temple_request")

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
                    val contributor = p0.children
                    for (data in contributor) {
                        if (data.key == contributorId) {
                            val contributorTemple = data.children
                            for (tContributorTemple in contributorTemple) {
                                val temple = tContributorTemple.getValue(Temple::class.java)
                                if (temple != null) {
                                    templeList.add(temple)
                                }
                            }
                        } else {
                            mTempleList.postValue(null)
                            return
                        }
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