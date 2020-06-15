package com.panjikrisnayasa.caripura.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.database.FirebaseDatabase
import com.panjikrisnayasa.caripura.model.Temple
import com.panjikrisnayasa.caripura.util.MySingleton
import org.json.JSONObject

class DeleteTempleViewModel : ViewModel() {

    fun deleteTempleRequest(temple: Temple) {
        temple.requestType = "delete"
        temple.requestStatus = "waiting"

        val updateRef =
            FirebaseDatabase.getInstance().getReference("temple").child("contributor_temple")
                .child(temple.contributorId).child(temple.id)
        updateRef.setValue(temple)

        val requestRef =
            FirebaseDatabase.getInstance().getReference("temple_request").child("delete_request")
                .child(temple.contributorId).child(temple.id)
        requestRef.setValue(temple)
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