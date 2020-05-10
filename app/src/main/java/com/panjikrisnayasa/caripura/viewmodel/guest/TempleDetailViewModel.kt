package com.panjikrisnayasa.caripura.viewmodel.guest

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.panjikrisnayasa.caripura.BuildConfig
import com.panjikrisnayasa.caripura.model.Temple
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class TempleDetailViewModel : ViewModel() {

    companion object {
        private const val BASE_URL_ROUTE = "https://maps.googleapis.com/maps/api/directions/json"
        private const val ORIGIN_PARAM = "origin"
        private const val DESTINATION_PARAM = "destination"
        private const val GOOGLE_API_KEY_PARAM = "key"
    }

    private lateinit var mDatabaseReference: DatabaseReference
    private val mTempleDetail = MutableLiveData<Temple>()
    private val mDistanceDuration = MutableLiveData<ArrayList<String>>()

    fun getTempleDetail(templeId: String): LiveData<Temple> {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("geo_fire")
        mDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val temple = p0.child(templeId).child("data").getValue(Temple::class.java)
                    if (temple != null) {
                        mTempleDetail.postValue(temple)
                    }
                }
            }
        })

        return mTempleDetail
    }

    fun getDistanceDuration(
        lastLat: String,
        lastLng: String,
        destinationLat: String,
        destinationLng: String
    ): LiveData<ArrayList<String>> {
        val client = AsyncHttpClient()
        val origin = "$lastLat,$lastLng"
        val destination = "$destinationLat,$destinationLng"
        val builtUri = Uri.parse(BASE_URL_ROUTE).buildUpon()
            .appendQueryParameter(ORIGIN_PARAM, origin)
            .appendQueryParameter(DESTINATION_PARAM, destination)
            .appendQueryParameter(
                GOOGLE_API_KEY_PARAM,
                BuildConfig.GOOGLE_API_KEY
            )
            .build()
        val url = builtUri.toString()
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                var tResult = ""
                if (responseBody != null) {
                    tResult = String(responseBody)
                }
                val resultRoute = tResult
                val responseObjectRoute = JSONObject(resultRoute)
                val routes = responseObjectRoute.getJSONArray("routes")
                val legs = routes.getJSONObject(0).getJSONArray("legs")
                val distance = legs.getJSONObject(0).getJSONObject("distance").getString("text")
                val duration = legs.getJSONObject(0).getJSONObject("duration").getString("text")
                val distanceDuration = arrayListOf<String>()
                distanceDuration.add(distance)
                distanceDuration.add(duration)
                mDistanceDuration.postValue(distanceDuration)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                error?.printStackTrace()
            }
        })
        return mDistanceDuration
    }
}