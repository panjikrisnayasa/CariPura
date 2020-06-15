package com.panjikrisnayasa.caripura.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.panjikrisnayasa.caripura.model.Temple

class TempleRequestListViewModel : ViewModel() {

    private val mTempleList = MutableLiveData<ArrayList<Temple>>()

    fun getAddTempleRequestList(): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("temple_request").child("add_request")
        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                p0.message
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    templeList.clear()
                    val contributorId = p0.children
                    for (tContributorId in contributorId) {
                        val templeRequest = tContributorId.children
                        for (tTempleRequest in templeRequest) {
                            val temple = tTempleRequest.getValue(Temple::class.java)
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

    fun getEditTempleRequestList(): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("temple_request").child("edit_request")
        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                p0.message
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    templeList.clear()
                    val contributorId = p0.children
                    for (tContributorId in contributorId) {
                        val templeRequest = tContributorId.children
                        for (tTempleRequest in templeRequest) {
                            val temple = tTempleRequest.getValue(Temple::class.java)
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

    fun getDeleteTempleRequestList(): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("temple_request").child("delete_request")
        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                p0.message
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    templeList.clear()
                    val contributorId = p0.children
                    for (tContributorId in contributorId) {
                        val templeRequest = tContributorId.children
                        for (tTempleRequest in templeRequest) {
                            val temple = tTempleRequest.getValue(Temple::class.java)
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
}