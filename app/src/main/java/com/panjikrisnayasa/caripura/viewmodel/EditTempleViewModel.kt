package com.panjikrisnayasa.caripura.viewmodel

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.panjikrisnayasa.caripura.model.Temple
import com.panjikrisnayasa.caripura.util.MySingleton
import org.json.JSONObject

class EditTempleViewModel : ViewModel() {

    private val mCode = MutableLiveData<String>()

    fun editTemple(oldTemple: Temple, newTemple: Temple): LiveData<String> {
        val templeRef: DatabaseReference
        val historyRef =
            FirebaseDatabase.getInstance().getReference("history").child("edit_request")
                .child(newTemple.contributorId)
        val geoFireRef =
            FirebaseDatabase.getInstance().getReference("geo_fire").child(newTemple.id)
                .child("data")

        if (newTemple.contributorId == "") {
            templeRef =
                FirebaseDatabase.getInstance().getReference("temple").child("admin_temple")
                    .child(newTemple.id)
        } else {
            newTemple.adminNote = ""
            newTemple.contributorNote = ""
            newTemple.requestStatus = "edited_by_admin"
            templeRef =
                FirebaseDatabase.getInstance().getReference("temple").child("contributor_temple")
                    .child(newTemple.contributorId).child(newTemple.id)
        }

        val oldPhotoName = oldTemple.photo.toUri().lastPathSegment
        val newPhotoName = newTemple.photo.toUri().lastPathSegment
        if (oldPhotoName != null && newPhotoName != null) {
            if (oldPhotoName != newPhotoName) {
                val storageRef =
                    FirebaseStorage.getInstance().getReference("temple_photos").child(newPhotoName)
                storageRef.putFile(newTemple.photo.toUri()).addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener {
                        val downloadUrl = it.toString()
                        newTemple.photo = downloadUrl
                        if (newTemple.contributorId != "") {
                            val key = historyRef.push().key
                            if (key != null)
                                historyRef.child(key).setValue(newTemple).addOnSuccessListener {
                                    mCode.postValue(key)
                                }
                        }
                        templeRef.setValue(newTemple)
                        geoFireRef.setValue(newTemple)
                    }
                }
            } else {
                if (newTemple.contributorId != "") {
                    val key = historyRef.push().key
                    if (key != null)
                        historyRef.child(key).setValue(newTemple).addOnSuccessListener {
                            mCode.postValue(key)
                        }
                }
                templeRef.setValue(newTemple)
                geoFireRef.setValue(newTemple)
            }
        }
        return mCode
    }

    fun editTempleRequest(oldTemple: Temple, newTemple: Temple): LiveData<String> {
        oldTemple.requestType = "edit"
        oldTemple.requestStatus = "waiting"
        newTemple.requestType = "edit"
        newTemple.requestStatus = "waiting"

        val updateRef =
            FirebaseDatabase.getInstance().getReference("temple").child("contributor_temple")
                .child(oldTemple.contributorId).child(oldTemple.id)
        val templeRequestRef =
            FirebaseDatabase.getInstance().getReference("temple_request").child("edit_request")
                .child(newTemple.contributorId).child(newTemple.id)

        val oldPhotoName = oldTemple.photo.toUri().lastPathSegment
        val newPhotoName = newTemple.photo.toUri().lastPathSegment
        if (oldPhotoName != null && newPhotoName != null) {
            if (oldPhotoName != newPhotoName) {
                val storageRef =
                    FirebaseStorage.getInstance().getReference("temple_photos").child(newPhotoName)
                storageRef.putFile(newTemple.photo.toUri()).addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener {
                        val downloadUrl = it.toString()
                        newTemple.photo = downloadUrl
                        updateRef.setValue(oldTemple)
                        templeRequestRef.setValue(newTemple)
                        mCode.postValue("1")
                    }
                }
            } else {
                updateRef.setValue(oldTemple)
                templeRequestRef.setValue(newTemple)
                mCode.postValue("1")
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