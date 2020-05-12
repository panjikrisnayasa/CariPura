package com.panjikrisnayasa.caripura.viewmodel.guest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.panjikrisnayasa.caripura.model.Temple

class TempleListViewModel : ViewModel() {

//    companion object {
//        private const val BASE_URL_DISTANCE = "https://maps.googleapis.com/maps/api/directions/json"
//        private const val ORIGIN_PARAM = "origin"
//        private const val DESTINATION_PARAM = "destination"
//        private const val GOOGLE_API_KEY_PARAM = "key"
//    }

    private lateinit var mDatabaseReference: DatabaseReference
    private val mTempleList = MutableLiveData<ArrayList<Temple>>()

    internal fun getTemple(): LiveData<ArrayList<Temple>> {
        val templeList = arrayListOf<Temple>()
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("geo_fire")
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
                    val temple = p0.child("data").getValue(Temple::class.java)
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

//    private fun getTempleDistance(
//        lastLat: String,
//        lastLng: String,
//        destinationLat: String,
//        destinationLng: String
//    ): String {
//        var templeDistance = ""
//        val client = AsyncHttpClient()
//        val origin = "$lastLat,$lastLng"
//        val destination = "$destinationLat,$destinationLng"
//        val builtUri = Uri.parse(BASE_URL_DISTANCE).buildUpon()
//            .appendQueryParameter(ORIGIN_PARAM, origin)
//            .appendQueryParameter(DESTINATION_PARAM, destination)
//            .appendQueryParameter(
//                GOOGLE_API_KEY_PARAM,
//                BuildConfig.GOOGLE_API_KEY
//            )
//            .build()
//        val url = builtUri.toString()
//        Log.d("hyperLoop", url)
//        client.get(url, object : AsyncHttpResponseHandler() {
//            override fun onSuccess(
//                statusCode: Int,
//                headers: Array<out Header>?,
//                responseBody: ByteArray?
//            ) {
//                var tResult = ""
//                if (responseBody != null) {
//                    tResult = String(responseBody)
//                }
//                val resultRoute = tResult
//                val responseObjectRoute = JSONObject(resultRoute)
//                val routes = responseObjectRoute.getJSONArray("routes")
//                val legs = routes.getJSONObject(0).getJSONArray("legs")
//                val distance = legs.getJSONObject(0).getJSONObject("distance")
//                    .getString("text")
//                Log.d("hyperLoop", "distance $distance")
//                templeDistance = distance
//            }
//
//            override fun onFailure(
//                statusCode: Int,
//                headers: Array<out Header>?,
//                responseBody: ByteArray?,
//                error: Throwable?
//            ) {
//                Log.d(
//                    "hyperLoop",
//                    "failed getting distance \n${error?.printStackTrace()}"
//                )
//            }
//        })
//        Log.d("hyperLoop", "templeDistance $templeDistance")
//        return templeDistance
//    }

}