package com.panjikrisnayasa.caripura.view.guest


import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQuery
import com.firebase.geofire.GeoQueryDataEventListener
import com.firebase.geofire.util.GeoUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.maps.android.PolyUtil
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.panjikrisnayasa.caripura.BuildConfig
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.model.Temple
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_find_temple.*
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class FindTempleFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val BASE_URL_ROUTE = "https://maps.googleapis.com/maps/api/directions/json"
        private const val ORIGIN_PARAM = "origin"
        private const val DESTINATION_PARAM = "destination"
        private const val GOOGLE_API_KEY_PARAM = "key"
    }

    private var mGoogleMap: GoogleMap? = null
    private var mTempleList: ArrayList<Temple> = arrayListOf()

    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mLastLocation: Location
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mGeoFire: GeoFire
    private lateinit var mGeoQuery: GeoQuery

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val tActivity = this.activity
        if (tActivity != null)
            mFusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(tActivity)
        return inflater.inflate(R.layout.fragment_find_temple, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("geo_fire")
        mGeoFire = GeoFire(mDatabaseReference)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.fragment_find_temple_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        floating_action_button_find_temple_my_location.setOnClickListener {
            val tLatLng = LatLng(mLastLocation.latitude, mLastLocation.longitude)
            mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(tLatLng, 16f))
        }

//        edit_cari_pura.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString("request_code", "cari_pura")
//            val daftarPuraFragment = DaftarPuraFragment()
//            daftarPuraFragment.arguments = bundle
//
//            activity?.supportFragmentManager?.beginTransaction()
//                ?.replace(
//                    R.id.frame_layout_main,
//                    daftarPuraFragment,
//                    DaftarPuraFragment::class.java.simpleName
//                )
//                ?.commit()
//
//            val bottomNav: BottomNavigationView? =
//                activity?.findViewById(R.id.bottom_navigation_view_main)
//            bottomNav?.selectedItemId = R.id.menu_bottom_navigation_daftar_pura
//        }

        button_find_temple_find_the_closest_temple.setOnClickListener {
            setGeoQuery(mLastLocation)
        }

//        mGeoFire.setLocation(
//            "temple_1", GeoLocation(-7.952918, 112.616029)
//        ) { _, error ->
//            if (error != null) {
//                Log.d("hyperLoop", "setting temple_1 location error: \n$error")
//            } else {
//                Log.d("hyperLoop", "setting temple_1 location success")
//            }
//        }
//        mGeoFire.setLocation(
//            "temple_2", GeoLocation(-7.952914, 112.616800)
//        ) { _, error ->
//            if (error != null) {
//                Log.d("hyperLoop", "setting temple_2 location error: \n$error")
//            } else {
//                Log.d("hyperLoop", "setting temple_2 location success")
//            }
//        }

    }

    override fun onMapReady(p0: GoogleMap?) {
        mGoogleMap = p0
        mGoogleMap?.setOnMarkerClickListener(this)

        setupMap()

        mGoogleMap?.isMyLocationEnabled = true
        mGoogleMap?.uiSettings?.isMyLocationButtonEnabled = false

        mFusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                mLastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                Log.d(
                    "hyperLoop",
                    "current lat = ${location.latitude}, long = ${location.longitude}"
                )
                mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
            }
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean = false

    private fun setupMap() {
        val tContext = this.context
        val tActivity = this.activity
        if (tContext != null && tActivity != null) {
            if (ActivityCompat.checkSelfPermission(
                    tContext,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    tActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
                return
            }
        }
    }

    private fun setGeoQuery(lastLocation: Location?) {
        if (lastLocation != null) {
            Log.d(
                "hyperLoop",
                "lastLocation lat = ${lastLocation.latitude}, long = ${lastLocation.longitude}"
            )
            mGeoQuery = mGeoFire.queryAtLocation(
                GeoLocation(
                    lastLocation.latitude,
                    lastLocation.longitude
                ), 3.0
            )
        }

        mGeoQuery.addGeoQueryDataEventListener(object : GeoQueryDataEventListener {
            override fun onGeoQueryReady() {
                Log.d("hyperLoop", "geo query ready")

                if (mTempleList.size > 0) {
                    val distanceTemple2: Double
                    val distanceTemple1 = GeoUtils.distance(
                        mTempleList[0].lat.toDouble(),
                        mTempleList[0].lng.toDouble(),
                        mLastLocation.latitude,
                        mLastLocation.longitude
                    )

                    if (mTempleList.size > 1) {
                        distanceTemple2 = GeoUtils.distance(
                            mTempleList[1].lat.toDouble(),
                            mTempleList[1].lng.toDouble(),
                            mLastLocation.latitude,
                            mLastLocation.longitude
                        )

                        if (distanceTemple1 < distanceTemple2) {
                            val latLng =
                                LatLng(
                                    mTempleList[0].lat.toDouble(),
                                    mTempleList[0].lng.toDouble()
                                )
                            mGoogleMap?.setInfoWindowAdapter(
                                CustomInfoWindowAdapter(
                                    context,
                                    mTempleList[0].photo,
                                    mTempleList[0].name
                                )
                            )
                            mGoogleMap?.addMarker(
                                MarkerOptions().position(latLng)
                                    .title(mTempleList[0].name)
                            )?.showInfoWindow()
                            getRoute(mTempleList[0].lat, mTempleList[0].lng)
                            mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
                        } else {
                            val latLng =
                                LatLng(mTempleList[1].lat.toDouble(), mTempleList[1].lng.toDouble())
                            mGoogleMap?.setInfoWindowAdapter(
                                CustomInfoWindowAdapter(
                                    context,
                                    mTempleList[1].photo,
                                    mTempleList[1].name
                                )
                            )
                            mGoogleMap?.addMarker(
                                MarkerOptions().position(latLng)
                                    .title(mTempleList[1].name)
                            )?.showInfoWindow()
                            getRoute(mTempleList[1].lat, mTempleList[1].lng)
                            mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
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
                        mTempleList.add(temple)
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

    private class CustomInfoWindowAdapter(context: Context?, photoUrl: String, templeName: String) :
        GoogleMap.InfoWindowAdapter {

        companion object {
            class MarkerCallback(var marker: Marker?) : Callback {

                override fun onSuccess() {
                    val tMarker = marker ?: return

                    if (!tMarker.isInfoWindowShown)
                        return

                    tMarker.hideInfoWindow()
                    tMarker.showInfoWindow()
                }

                override fun onError(e: Exception?) {
                    e?.printStackTrace()
                }
            }
        }

        private var tPhotoUrl: String = photoUrl
        private var tTempleName: String = templeName
        private var viewGroup: ViewGroup? = null
        private var layoutInflater =
            context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        private var view = layoutInflater.inflate(R.layout.info_window_custom, viewGroup, false)

        override fun getInfoContents(p0: Marker?): View? {
            val imageView = view.findViewById<ImageView>(R.id.image_info_window_custom)
            val textView = view.findViewById<TextView>(R.id.text_info_window_custom)

            Picasso.get().load(tPhotoUrl).resize(1000, 1000).onlyScaleDown()
                .centerCrop().into(imageView,
                    MarkerCallback(
                        p0
                    )
                )
            textView.text = tTempleName

            return view
        }


        override fun getInfoWindow(p0: Marker?): View? {
            return null
        }
    }

    private fun getRoute(destinationLat: String, destinationLng: String) {
        val client = AsyncHttpClient()
        val origin = "${mLastLocation.latitude},${mLastLocation.longitude}"
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
                    if (i == 0) {
                        mGoogleMap?.addPolyline(
                            PolylineOptions().addAll(path[i]).width(16f)
                                .startCap(CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.ic_route_start_end_white_8dp)))
                                .endCap(RoundCap()).jointType(JointType.ROUND)
                                .color(0xFF4285F4.toInt()).geodesic(true)
                        )
                    } else if (i == path.size - 1) {
                        mGoogleMap?.addPolyline(
                            PolylineOptions().addAll(path[i]).width(16f).startCap(RoundCap())
                                .endCap(CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.ic_route_start_end_white_8dp)))
                                .jointType(JointType.ROUND)
                                .color(0xFF4285F4.toInt()).geodesic(true)
                        )
                    } else {
                        mGoogleMap?.addPolyline(
                            PolylineOptions().addAll(path[i]).width(16f).startCap(RoundCap())
                                .endCap(RoundCap()).jointType(JointType.ROUND)
                                .color(0xFF4285F4.toInt()).geodesic(true)
                        )
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

