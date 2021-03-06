package com.panjikrisnayasa.caripura.viewmodel

import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQuery
import com.firebase.geofire.GeoQueryDataEventListener
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.maps.android.PolyUtil
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.panjikrisnayasa.caripura.BuildConfig
import com.panjikrisnayasa.caripura.model.Temple
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class FindTempleViewModel : ViewModel() {

    companion object {
        const val BASE_URL_ROUTE = "https://maps.googleapis.com/maps/api/directions/json"
        const val ORIGIN_PARAM = "origin"
        const val DESTINATION_PARAM = "destination"
        const val GOOGLE_API_KEY_PARAM = "key"
    }

    private val mTempleList = MutableLiveData<ArrayList<Temple>>()
    private val mPath = MutableLiveData<MutableList<List<LatLng>>>()
    private val mDistanceDuration = MutableLiveData<ArrayList<String>>()

    fun setGeoQuery(
        lastLocation: Location?
    ): LiveData<ArrayList<Temple>> {
        lateinit var geoQuery: GeoQuery
        val databaseReference = FirebaseDatabase.getInstance().getReference("geo_fire")
        val geoFire = GeoFire(databaseReference)
        val templeList = arrayListOf<Temple>()
        if (lastLocation != null) {
            geoQuery = geoFire.queryAtLocation(
                GeoLocation(
                    lastLocation.latitude,
                    lastLocation.longitude
                ), 7.0
            )

            geoQuery.addGeoQueryDataEventListener(object : GeoQueryDataEventListener {
                override fun onGeoQueryReady() {
                    if (templeList.size == 0) {
                        mTempleList.postValue(null)
                    } else {
                        mTempleList.postValue(templeList)
                    }
                }

                override fun onDataExited(dataSnapshot: DataSnapshot?) {
                }

                override fun onDataChanged(dataSnapshot: DataSnapshot?, location: GeoLocation?) {
                }

                override fun onDataEntered(dataSnapshot: DataSnapshot?, location: GeoLocation?) {
                    if (dataSnapshot != null && location != null) {
                        val data = dataSnapshot.child("data")
                        val temple = data.getValue(Temple::class.java)
                        if (temple != null) {
                            templeList.add(temple)
                        }
                    }
                }

                override fun onDataMoved(dataSnapshot: DataSnapshot?, location: GeoLocation?) {
                }

                override fun onGeoQueryError(error: DatabaseError?) {
                    error?.message
                }
            })
        }
        return mTempleList
    }

    fun setDirectionApi(
        lastLat: String,
        lastLng: String,
        destinationLat: String,
        destinationLng: String
    ) {
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
        val path: MutableList<List<LatLng>> = ArrayList()
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val tResult: String
                val distanceDuration = arrayListOf<String>()
                if (responseBody != null) {
                    tResult = String(responseBody)
                    val responseObjectRoute = JSONObject(tResult)
                    val routes = responseObjectRoute.getJSONArray("routes")
                    if (routes.length() > 0) {
                        Log.d("hyperLoop", "routes length > 0")
                        val legs = routes.getJSONObject(0)?.getJSONArray("legs")
                        val steps = legs?.getJSONObject(0)?.getJSONArray("steps")
                        val distance =
                            legs?.getJSONObject(0)?.getJSONObject("distance")?.getString("text")
                        val duration =
                            legs?.getJSONObject(0)?.getJSONObject("duration")?.getString("text")
                        if (distance != null && duration != null) {
                            distanceDuration.add(distance)
                            distanceDuration.add(duration)
                        }

                        if (steps != null)
                            for (i in 0 until steps.length()) {
                                val points =
                                    steps.getJSONObject(i)?.getJSONObject("polyline")
                                        ?.getString("points")
                                path.add(PolyUtil.decode(points))
                            }
                    } else {
                        Log.d("hyperLoop", "routes length < 0")
                        return
                    }
                    mDistanceDuration.postValue(distanceDuration)
                    mPath.postValue(path)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                setDirectionApi(lastLat, lastLng, destinationLat, destinationLng)
            }
        })
    }

    fun getDistanceDuration(): LiveData<ArrayList<String>> {
        return mDistanceDuration
    }

    fun getPath(): LiveData<MutableList<List<LatLng>>> {
        return mPath
    }
}