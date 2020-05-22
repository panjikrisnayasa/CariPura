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
        mDatabaseReference.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                p0.message
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    templeList.clear()
                    val contributorId = p0.children
                    for (tContributorId in contributorId) {
                        val templeId = tContributorId.children
                        for (tTempleId in templeId) {
                            val temple = tTempleId.getValue(Temple::class.java)
                            if (temple != null)
                                templeList.add(temple)
                        }
                    }
                    mTempleList.postValue(templeList)
                } else {
                    mTempleList.postValue(null)
                }
            }
        })
        return mTempleList
    }

    fun getEditTempleApprovalHistory(): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        mDatabaseReference =
            FirebaseDatabase.getInstance().getReference("history").child("edit_request")
        mDatabaseReference.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                p0.message
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    templeList.clear()
                    val contributorId = p0.children
                    for (tContributorId in contributorId) {
                        val templeId = tContributorId.children
                        for (tTempleId in templeId) {
                            val temple = tTempleId.getValue(Temple::class.java)
                            if (temple != null)
                                templeList.add(temple)
                        }
                    }
                    mTempleList.postValue(templeList)
                } else {
                    mTempleList.postValue(null)
                }
            }
        })
        return mTempleList
    }

    fun getDeleteTempleApprovalHistory(): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        mDatabaseReference =
            FirebaseDatabase.getInstance().getReference("history").child("delete_request")
        mDatabaseReference.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                p0.message
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    templeList.clear()
                    val contributorId = p0.children
                    for (tContributorId in contributorId) {
                        val templeId = tContributorId.children
                        for (tTempleId in templeId) {
                            val temple = tTempleId.getValue(Temple::class.java)
                            if (temple != null)
                                templeList.add(temple)
                        }
                    }
                    mTempleList.postValue(templeList)
                } else {
                    mTempleList.postValue(null)
                }
            }
        })
        return mTempleList
    }

    fun getAddTempleRequestHistory(contributorId: String): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        mDatabaseReference =
            FirebaseDatabase.getInstance().getReference("history").child("add_request")
                .child(contributorId)
        mDatabaseReference.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                p0.message
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    templeList.clear()
                    val templeId = p0.children
                    for (tTempleId in templeId) {
                        val temple = tTempleId.getValue(Temple::class.java)
                        if (temple != null)
                            templeList.add(temple)
                    }
                    mTempleList.postValue(templeList)
                } else {
                    mTempleList.postValue(null)
                }
            }
        })
        return mTempleList
    }

    fun getEditTempleRequestHistory(contributorId: String): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        mDatabaseReference =
            FirebaseDatabase.getInstance().getReference("history").child("edit_request").child(contributorId)
        mDatabaseReference.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                p0.message
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    templeList.clear()
                    val templeId = p0.children
                    for (tTempleId in templeId) {
                        val temple = tTempleId.getValue(Temple::class.java)
                        if (temple != null)
                            templeList.add(temple)
                    }
                    mTempleList.postValue(templeList)
                } else {
                    mTempleList.postValue(null)
                }
            }
        })
        return mTempleList
    }

    fun getDeleteTempleRequestHistory(contributorId: String): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        mDatabaseReference =
            FirebaseDatabase.getInstance().getReference("history").child("delete_request").child(contributorId)
        mDatabaseReference.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                p0.message
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    templeList.clear()
                    val templeId = p0.children
                    for (tTempleId in templeId) {
                        val temple = tTempleId.getValue(Temple::class.java)
                        if (temple != null)
                            templeList.add(temple)
                    }
                    mTempleList.postValue(templeList)
                } else {
                    mTempleList.postValue(null)
                }
            }
        })
        return mTempleList
    }
}