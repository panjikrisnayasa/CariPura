package com.panjikrisnayasa.caripura.view


import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQuery
import com.firebase.geofire.GeoQueryDataEventListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.model.Temple
import kotlinx.android.synthetic.main.fragment_find_temple.*

/**
 * A simple [Fragment] subclass.
 */
class FindTempleFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
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
                ), 1.0
            )
        }

        mGeoQuery.addGeoQueryDataEventListener(object : GeoQueryDataEventListener {
            override fun onGeoQueryReady() {
                Log.d("hyperLoop", "geo query ready")

                val latLng = LatLng(mTempleList[0].lat.toDouble(), mTempleList[0].lng.toDouble())
                mGoogleMap?.addMarker(MarkerOptions().position(latLng).title(mTempleList[0].name))
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
}
