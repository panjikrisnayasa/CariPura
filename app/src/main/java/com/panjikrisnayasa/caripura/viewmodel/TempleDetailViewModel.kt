package com.panjikrisnayasa.caripura.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.panjikrisnayasa.caripura.BuildConfig
import com.panjikrisnayasa.caripura.model.Temple
import com.panjikrisnayasa.caripura.util.MySingleton
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class TempleDetailViewModel : ViewModel() {

    private val mDistanceDuration = MutableLiveData<ArrayList<String>>()
    private val mTemple = MutableLiveData<Temple>()
    private val mCode = MutableLiveData<String>()

    fun getDistanceDuration(
        lastLat: String,
        lastLng: String,
        destinationLat: String,
        destinationLng: String
    ): LiveData<ArrayList<String>> {
        val client = AsyncHttpClient()
        val origin = "$lastLat,$lastLng"
        val destination = "$destinationLat,$destinationLng"
        val builtUri = Uri.parse(FindTempleViewModel.BASE_URL_ROUTE).buildUpon()
            .appendQueryParameter(FindTempleViewModel.ORIGIN_PARAM, origin)
            .appendQueryParameter(FindTempleViewModel.DESTINATION_PARAM, destination)
            .appendQueryParameter(
                FindTempleViewModel.GOOGLE_API_KEY_PARAM,
                BuildConfig.GOOGLE_API_KEY
            )
            .build()
        val url = builtUri.toString()
        Log.d("hyperLoop", url)
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val tResult: String
                val distanceDuration = arrayListOf<String>()
                if (responseBody != null) {
                    Log.d("hyperLoop", "statusCode = $statusCode")
                    Log.d("hyperLoop", "headers = $headers")
                    Log.d("hyperLoop", "responseBody = $responseBody")
                    tResult = String(responseBody)
                    Log.d("hyperLoop", tResult)
                    val responseObjectRoute = JSONObject(tResult)
                    Log.d(
                        "hyperLoop",
                        "responseObjectRoute.length() = ${responseObjectRoute.length()}"
                    )
                    val routes = responseObjectRoute.getJSONArray("routes")
                    if (routes.length() > 0) {
                        Log.d("hyperLoop", "routes.length() = ${routes.length()}")
                        val legs = routes.getJSONObject(0)?.getJSONArray("legs")
                        val distance =
                            legs?.getJSONObject(0)?.getJSONObject("distance")?.getString("text")
                        val duration =
                            legs?.getJSONObject(0)?.getJSONObject("duration")?.getString("text")
                        if (distance != null && duration != null) {
                            distanceDuration.add(distance)
                            distanceDuration.add(duration)
                        }
                    } else {
                        Log.d("hyperLoop", "routes length < 0")
                        return
                    }
                    mDistanceDuration.postValue(distanceDuration)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                getDistanceDuration(lastLat, lastLng, destinationLat, destinationLng)
            }
        })
        return mDistanceDuration
    }

    fun getTempleDetail(templeId: String): LiveData<Temple> {
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("geo_fire").child(templeId)
                .child("data")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                p0.message
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val temple = p0.getValue(Temple::class.java)
                    if (temple != null)
                        mTemple.postValue(temple)
                } else {
                    mTemple.postValue(null)
                }
            }
        })
        return mTemple
    }

    fun getTempleRequestHistoryDetailContributor(
        requestType: String,
        contributorId: String,
        templeId: String
    ): LiveData<Temple> {
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("history").child(requestType)
                .child(contributorId).child(templeId)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                p0.message
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val temple = p0.getValue(Temple::class.java)
                    if (temple != null)
                        mTemple.postValue(temple)
                } else {
                    mTemple.postValue(null)
                }
            }
        })
        return mTemple
    }

    fun getTempleRequestDetail(
        requestType: String,
        contributorId: String,
        templeId: String
    ): LiveData<Temple> {
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("temple_request").child(requestType)
                .child(contributorId).child(templeId)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                p0.message
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val temple = p0.getValue(Temple::class.java)
                    if (temple != null)
                        mTemple.postValue(temple)
                } else {
                    mTemple.postValue(null)
                }
            }
        })
        return mTemple
    }

    fun deleteTemple(temple: Temple): LiveData<String> {
        if (temple.contributorId == "") {
            val templeRef =
                FirebaseDatabase.getInstance().getReference("temple").child("admin_temple")
                    .child(temple.id)
            templeRef.removeValue()
            mCode.postValue("1")
        } else {
            temple.adminNote = ""
            temple.contributorNote = ""
            val templeRef =
                FirebaseDatabase.getInstance().getReference("temple").child("contributor_temple")
                    .child(temple.contributorId).child(temple.id)
            templeRef.removeValue()

            temple.requestStatus = "deleted_by_admin"
            val historyRef =
                FirebaseDatabase.getInstance().getReference("history").child("delete_request")
                    .child(temple.contributorId)
            val key = historyRef.push().key
            if (key != null)
                historyRef.child(key).setValue(temple).addOnSuccessListener {
                    mCode.postValue(key)
                }
        }

        val geoFireRef = FirebaseDatabase.getInstance().getReference("geo_fire").child(temple.id)
        geoFireRef.removeValue()

        return mCode
    }

    fun cancelTempleRequest(temple: Temple) {
        when (temple.requestType) {
            "add" -> {
                val requestRef =
                    FirebaseDatabase.getInstance().getReference("temple_request")
                        .child("add_request")
                        .child(temple.contributorId)
                        .child(temple.id)
                requestRef.removeValue()
            }
            "edit" -> {
                val databaseReference =
                    FirebaseDatabase.getInstance().getReference("temple_request")
                        .child("edit_request")
                        .child(temple.contributorId)
                        .child(temple.id)
                databaseReference.removeValue()

                val templeRef = FirebaseDatabase.getInstance().getReference("temple")
                    .child("contributor_temple")
                    .child(temple.contributorId)
                    .child(temple.id)
                templeRef.child("requestType").setValue("")
                templeRef.child("requestStatus").setValue("")
            }
            else -> {
                val databaseReference =
                    FirebaseDatabase.getInstance().getReference("temple_request")
                        .child("delete_request")
                        .child(temple.contributorId)
                        .child(temple.id)
                databaseReference.removeValue()

                val templeRef = FirebaseDatabase.getInstance().getReference("temple")
                    .child("contributor_temple")
                    .child(temple.contributorId)
                    .child(temple.id)
                templeRef.child("requestType").setValue("")
                templeRef.child("requestStatus").setValue("")
            }
        }
    }

    fun addRequestAccepted(temple: Temple): LiveData<String> {
        temple.requestStatus = "accepted"
        val historyRef =
            FirebaseDatabase.getInstance().getReference("history").child("add_request")
                .child(temple.contributorId).child(temple.id)
        historyRef.setValue(temple).addOnSuccessListener {
            mCode.postValue("done")
        }

        temple.requestStatus = ""
        temple.requestType = ""
        val templeRef =
            FirebaseDatabase.getInstance().getReference("temple").child("contributor_temple")
                .child(temple.contributorId).child(temple.id)
        templeRef.setValue(temple)

        val geoFireRef = FirebaseDatabase.getInstance().getReference("geo_fire")
        val geoFire = GeoFire(geoFireRef)

        geoFire.setLocation(temple.id, GeoLocation(temple.lat.toDouble(), temple.lng.toDouble()))
        geoFireRef.child(temple.id).child("data").setValue(temple)

        val templeRequestRef =
            FirebaseDatabase.getInstance().getReference("temple_request").child("add_request")
                .child(temple.contributorId).child(temple.id)
        templeRequestRef.removeValue()
        return mCode
    }

    fun addRequestRejected(temple: Temple): LiveData<String> {
        temple.requestStatus = "rejected"
        val historyRef =
            FirebaseDatabase.getInstance().getReference("history").child("add_request")
                .child(temple.contributorId).child(temple.id)
        historyRef.setValue(temple).addOnSuccessListener {
            mCode.postValue("done")
        }

        val templeRequestRef =
            FirebaseDatabase.getInstance().getReference("temple_request").child("add_request")
                .child(temple.contributorId).child(temple.id)
        templeRequestRef.removeValue()
        return mCode
    }

    fun editRequestAccepted(temple: Temple): LiveData<String> {
        temple.requestStatus = "accepted"
        val historyRef =
            FirebaseDatabase.getInstance().getReference("history").child("edit_request")
                .child(temple.contributorId)
        val key = historyRef.push().key
        if (key != null)
            historyRef.child(key).setValue(temple).addOnSuccessListener {
                mCode.postValue(key)
            }

        temple.requestStatus = ""
        temple.requestType = ""
        val templeRef =
            FirebaseDatabase.getInstance().getReference("temple").child("contributor_temple")
                .child(temple.contributorId).child(temple.id)
        templeRef.setValue(temple)

        val geoFireRef = FirebaseDatabase.getInstance().getReference("geo_fire")
            .child(temple.id)
        geoFireRef.child("data").setValue(temple)
        geoFireRef.child("l").child("0").setValue(temple.lat.toDouble())
        geoFireRef.child("l").child("1").setValue(temple.lng.toDouble())

        val templeRequestRef =
            FirebaseDatabase.getInstance().getReference("temple_request").child("edit_request")
                .child(temple.contributorId).child(temple.id)
        templeRequestRef.removeValue()
        return mCode
    }

    fun editRequestRejected(temple: Temple): LiveData<String> {
        temple.requestStatus = "rejected"
        val historyRef =
            FirebaseDatabase.getInstance().getReference("history").child("edit_request")
                .child(temple.contributorId)
        val key = historyRef.push().key
        if (key != null)
            historyRef.child(key).setValue(temple).addOnSuccessListener {
                mCode.postValue(key)
            }

        val templeRef = FirebaseDatabase.getInstance().getReference("temple")
            .child("contributor_temple")
            .child(temple.contributorId)
            .child(temple.id)
        templeRef.child("requestType").setValue("")
        templeRef.child("requestStatus").setValue("")

        val templeRequestRef =
            FirebaseDatabase.getInstance().getReference("temple_request").child("edit_request")
                .child(temple.contributorId).child(temple.id)
        templeRequestRef.removeValue()
        return mCode
    }

    fun deleteRequestAccepted(temple: Temple): LiveData<String> {
        temple.requestStatus = "accepted"
        val historyRef =
            FirebaseDatabase.getInstance().getReference("history").child("delete_request")
                .child(temple.contributorId)
        val key = historyRef.push().key
        if (key != null)
            historyRef.child(key).setValue(temple).addOnSuccessListener {
                mCode.postValue(key)
            }

        val templeRef = FirebaseDatabase.getInstance().getReference("temple")
            .child("contributor_temple")
            .child(temple.contributorId)
            .child(temple.id)
        templeRef.removeValue()

        val geoFireRef = FirebaseDatabase.getInstance().getReference("geo_fire")
            .child(temple.id)
        geoFireRef.removeValue()

        val templeRequestRef =
            FirebaseDatabase.getInstance().getReference("temple_request").child("delete_request")
                .child(temple.contributorId).child(temple.id)
        templeRequestRef.removeValue()
        return mCode
    }

    fun deleteRequestRejected(temple: Temple): LiveData<String> {
        temple.requestStatus = "rejected"
        val historyRef =
            FirebaseDatabase.getInstance().getReference("history").child("delete_request")
                .child(temple.contributorId)
        val key = historyRef.push().key
        if (key != null)
            historyRef.child(key).setValue(temple).addOnSuccessListener {
                mCode.postValue(key)
            }

        val templeRef = FirebaseDatabase.getInstance().getReference("temple")
            .child("contributor_temple")
            .child(temple.contributorId)
            .child(temple.id)
        templeRef.child("requestType").setValue("")
        templeRef.child("requestStatus").setValue("")

        val templeRequestRef =
            FirebaseDatabase.getInstance().getReference("temple_request").child("delete_request")
                .child(temple.contributorId).child(temple.id)
        templeRequestRef.removeValue()
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