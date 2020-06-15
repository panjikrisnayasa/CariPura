package com.panjikrisnayasa.caripura.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.panjikrisnayasa.caripura.model.Temple

class TempleListViewModel : ViewModel() {

    private val mTempleList = MutableLiveData<ArrayList<Temple>>()

    fun getTempleList(): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        val databaseReference = FirebaseDatabase.getInstance().getReference("geo_fire")
        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                p0.message
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    templeList.clear()
                    val templeId = p0.children
                    for (tTempleId in templeId) {
                        val temple = tTempleId.child("data").getValue(Temple::class.java)
                        if (temple != null) {
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