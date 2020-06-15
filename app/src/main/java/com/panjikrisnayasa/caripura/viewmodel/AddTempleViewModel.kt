package com.panjikrisnayasa.caripura.viewmodel

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.panjikrisnayasa.caripura.model.Temple
import com.panjikrisnayasa.caripura.util.MySingleton
import org.json.JSONObject

class AddTempleViewModel : ViewModel() {

    private var mCode = MutableLiveData<Int>()

    fun addTemple(temple: Temple): LiveData<Int> {
        val templeRef =
            FirebaseDatabase.getInstance().getReference("temple").child("admin_temple")
        val key = templeRef.push().key
        if (key != null) {
            temple.id = key
            val photoName = temple.photo.toUri().lastPathSegment
            if (photoName != null) {
                val storageRef =
                    FirebaseStorage.getInstance().getReference("temple_photos").child(photoName)
                storageRef.putFile(temple.photo.toUri()).addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener {
                        val downloadUrl = it.toString()
                        temple.photo = downloadUrl
                        templeRef.child(key).setValue(temple)
                        val geoFireRef =
                            FirebaseDatabase.getInstance().getReference("geo_fire")
                        val geoFire = GeoFire(geoFireRef)
                        geoFire.setLocation(
                            key,
                            GeoLocation(temple.lat.toDouble(), temple.lng.toDouble())
                        )
                        geoFireRef.child(key).child("data").setValue(temple)
                        mCode.postValue(1)
                    }
                }
            }
        }
        return mCode
    }

    fun addTempleRequest(temple: Temple): LiveData<Int> {
        temple.requestType = "add"
        temple.requestStatus = "waiting"
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("temple_request").child("add_request")
                .child(temple.contributorId)
        val key = databaseReference.push().key
        if (key != null) {
            temple.id = key
            val photoName = temple.photo.toUri().lastPathSegment
            if (photoName != null) {
                val storageRef =
                    FirebaseStorage.getInstance().getReference("temple_photos").child(photoName)
                storageRef.putFile(temple.photo.toUri()).addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener {
                        val downloadUrl = it.toString()
                        temple.photo = downloadUrl
                        databaseReference.child(key).setValue(temple)
                        mCode.postValue(1)
                    }
                }
            }
        }
        return mCode
    }

    fun sendNotification(notification: JSONObject, context: Context) {
        val fcmApi = "https://fcm.googleapis.com/fcm/send"
        val serverKey =
            "key=" + "AAAASake7g4:APA91bG-nKVePjIQdtGYrMlnR0XPxJxxEFbWZRNMH-4XXfZqBsIEARzmKA8TexitmpVbWeAuZ-9qj36b4iKQKv_xapoiMYWSAognGSp1yRZbTCdCGBv2t_INClwIRC1I5Mbt7iMUn5Qt"
        val contentType = "application/json"

        val jsonObjectRequest = object : JsonObjectRequest(fcmApi, notification,
            Response.Listener {
            },
            Response.ErrorListener {
            }) {

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        MySingleton.getInstance(context)?.addToRequestQueue(jsonObjectRequest)
    }
}