package com.panjikrisnayasa.caripura.viewmodel.guest

import android.content.Context
import android.location.Location
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQuery
import com.firebase.geofire.GeoQueryDataEventListener
import com.firebase.geofire.util.GeoUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.maps.android.PolyUtil
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.panjikrisnayasa.caripura.BuildConfig
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.model.Temple
import com.panjikrisnayasa.caripura.view.guest.FindTempleFragment
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class FindTempleViewModel : ViewModel() {

    companion object {
        private const val BASE_URL_ROUTE = "https://maps.googleapis.com/maps/api/directions/json"
        private const val ORIGIN_PARAM = "origin"
        private const val DESTINATION_PARAM = "destination"
        private const val GOOGLE_API_KEY_PARAM = "key"
    }

    fun setGeoQuery(lastLocation: Location?, context: Context?, googleMap: GoogleMap?) {
        lateinit var geoQuery: GeoQuery
        val databaseReference = FirebaseDatabase.getInstance().getReference("geo_fire")
        val geoFire = GeoFire(databaseReference)
        val templeList = arrayListOf<Temple>()
        val lastLat = lastLocation?.latitude.toString()
        val lastLng = lastLocation?.longitude.toString()

        if (lastLocation != null) {
            Log.d(
                "hyperLoop",
                "lastLocation lat = ${lastLocation.latitude}, long = ${lastLocation.longitude}"
            )
            geoQuery = geoFire.queryAtLocation(
                GeoLocation(
                    lastLocation.latitude,
                    lastLocation.longitude
                ), 3.0
            )

            geoQuery.addGeoQueryDataEventListener(object : GeoQueryDataEventListener {
                override fun onGeoQueryReady() {
                    Log.d("hyperLoop", "geo query ready")

                    if (templeList.size > 0) {
                        val distanceTemple2: Double
                        val distanceTemple1 = GeoUtils.distance(
                            templeList[0].lat.toDouble(),
                            templeList[0].lng.toDouble(),
                            lastLocation.latitude,
                            lastLocation.longitude
                        )

                        if (templeList.size > 1) {
                            distanceTemple2 = GeoUtils.distance(
                                templeList[1].lat.toDouble(),
                                templeList[1].lng.toDouble(),
                                lastLocation.latitude,
                                lastLocation.longitude
                            )

                            if (distanceTemple1 < distanceTemple2) {
                                val latLng =
                                    LatLng(
                                        templeList[0].lat.toDouble(),
                                        templeList[0].lng.toDouble()
                                    )
                                googleMap?.setInfoWindowAdapter(
                                    FindTempleFragment.CustomInfoWindowAdapter(
                                        context,
                                        templeList[0].photo,
                                        templeList[0].name
                                    )
                                )
                                googleMap?.addMarker(
                                    MarkerOptions().position(latLng)
                                        .title(templeList[0].name)
                                )?.showInfoWindow()
                                getRoute(
                                    googleMap,
                                    lastLat,
                                    lastLng,
                                    templeList[0].lat,
                                    templeList[0].lng
                                )
                                googleMap?.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        latLng,
                                        18f
                                    )
                                )
                            } else {
                                val latLng =
                                    LatLng(
                                        templeList[1].lat.toDouble(),
                                        templeList[1].lng.toDouble()
                                    )
                                googleMap?.setInfoWindowAdapter(
                                    FindTempleFragment.CustomInfoWindowAdapter(
                                        context,
                                        templeList[1].photo,
                                        templeList[1].name
                                    )
                                )
                                googleMap?.addMarker(
                                    MarkerOptions().position(latLng)
                                        .title(templeList[1].name)
                                )?.showInfoWindow()
                                getRoute(
                                    googleMap,
                                    lastLat,
                                    lastLng,
                                    templeList[1].lat,
                                    templeList[1].lng
                                )
                                googleMap?.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        latLng,
                                        18f
                                    )
                                )
                            }
                        }
                    }
                }

                override fun onDataExited(dataSnapshot: DataSnapshot?) {
                    TODO("Not yet implemented")
                }

                override fun onDataChanged(dataSnapshot: DataSnapshot?, location: GeoLocation?) {
                    TODO("Not yet implemented")
                }

                override fun onDataEntered(dataSnapshot: DataSnapshot?, location: GeoLocation?) {
                    Log.d("hyperLoop", "key entered")
                    if (dataSnapshot != null && location != null) {
                        val data = dataSnapshot.child("data")
                        Log.d(
                            "hyperLoop",
                            "$data entered: ${location.latitude}, ${location.longitude}"
                        )

                        val temple = data.getValue(Temple::class.java)
                        if (temple != null) {
                            templeList.add(temple)
                            Toast.makeText(
                                context,
                                "onDataEntered: ${temple.name} \n${temple.lat}, ${temple.lng}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onDataMoved(dataSnapshot: DataSnapshot?, location: GeoLocation?) {
                    TODO("Not yet implemented")
                }

                override fun onGeoQueryError(error: DatabaseError?) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    fun getRoute(
        googleMap: GoogleMap?,
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
        Log.d("hyperLoop", url)
        val path: MutableList<List<LatLng>> = ArrayList()
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
                val steps = legs.getJSONObject(0).getJSONArray("steps")
                for (i in 0 until steps.length()) {
                    val points =
                        steps.getJSONObject(i).getJSONObject("polyline")
                            .getString("points")
                    path.add(PolyUtil.decode(points))
                }
                for (i in 0 until path.size) {
                    when (i) {
                        0 -> {
                            googleMap?.addPolyline(
                                PolylineOptions().addAll(path[i]).width(16f)
                                    .startCap(CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.ic_route_start_end_white_8dp)))
                                    .endCap(RoundCap()).jointType(JointType.ROUND)
                                    .color(0xFF4285F4.toInt()).geodesic(true)
                            )
                        }
                        path.size - 1 -> {
                            googleMap?.addPolyline(
                                PolylineOptions().addAll(path[i]).width(16f).startCap(RoundCap())
                                    .endCap(CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.ic_route_start_end_white_8dp)))
                                    .jointType(JointType.ROUND)
                                    .color(0xFF4285F4.toInt()).geodesic(true)
                            )
                        }
                        else -> {
                            googleMap?.addPolyline(
                                PolylineOptions().addAll(path[i]).width(16f).startCap(RoundCap())
                                    .endCap(RoundCap()).jointType(JointType.ROUND)
                                    .color(0xFF4285F4.toInt()).geodesic(true)
                            )
                        }
                    }
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d(
                    "hyperLoop",
                    "failed getting route \n${error?.printStackTrace()}"
                )
            }
        })
    }
}