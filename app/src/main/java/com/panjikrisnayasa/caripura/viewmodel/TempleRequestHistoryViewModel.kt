package com.panjikrisnayasa.caripura.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.panjikrisnayasa.caripura.model.Temple

class TempleRequestHistoryViewModel : ViewModel() {

    private val mTempleList = MutableLiveData<ArrayList<Temple>>()

    fun getAddTempleApprovalHistory(): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("history").child("add_request")
        databaseReference.addValueEventListener(object : ValueEventListener {

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
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("history").child("edit_request")
        databaseReference.addValueEventListener(object : ValueEventListener {

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
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("history").child("delete_request")
        databaseReference.addValueEventListener(object : ValueEventListener {

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
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("history").child("add_request")
                .child(contributorId)
        databaseReference.addValueEventListener(object : ValueEventListener {

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
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("history").child("edit_request")
                .child(contributorId)
        databaseReference.addValueEventListener(object : ValueEventListener {

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
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("history").child("delete_request")
                .child(contributorId)
        databaseReference.addValueEventListener(object : ValueEventListener {

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