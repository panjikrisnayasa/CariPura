package com.panjikrisnayasa.caripura.view


import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQuery
import com.firebase.geofire.GeoQueryEventListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.model.Temple
import kotlinx.android.synthetic.main.fragment_find_temple.*

/**
 * A simple [Fragment] subclass.
 */
class FindTempleFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    ValueEventListener {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private var mGoogleMap: GoogleMap? = null
    private var mTempleList: ArrayList<Temple> = arrayListOf()

    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mLastLocation: Location
    private lateinit var mDatabaseReference: DatabaseReference
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

        mDatabaseReference = FirebaseDatabase.getInstance().reference
        mDatabaseReference.child("pura").addListenerForSingleValueEvent(this)

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
                mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
            }
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean = false

    override fun onCancelled(p0: DatabaseError) {}

    override fun onDataChange(p0: DataSnapshot) {
        for (data in p0.children) {
            val temple = data.getValue(Temple::class.java)
            if (temple != null) {
                GeoFire(mDatabaseReference).setLocation(
                    "geofire",
                    GeoLocation(temple.lat.toDouble(), temple.long.toDouble())
                )
                mTempleList.add(temple)
            }
        }
    }

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
            mGeoQuery = GeoFire(mDatabaseReference).queryAtLocation(
                GeoLocation(
                    lastLocation.latitude,
                    lastLocation.longitude
                ), 0.3
            )
        }

        mGeoQuery.addGeoQueryEventListener(object : GeoQueryEventListener {
            override fun onGeoQueryReady() {}

            override fun onKeyEntered(key: String?, location: GeoLocation?) {
                for (temple in mTempleList) {
                    if (temple.lat.toDouble() == location?.latitude && temple.long.toDouble() == location.longitude) {
                        val latLng = LatLng(temple.lat.toDouble(), temple.long.toDouble())
                        mGoogleMap?.addMarker(MarkerOptions().position(latLng).title(temple.name))
                    }
                }
            }

            override fun onKeyMoved(key: String?, location: GeoLocation?) {}

            override fun onKeyExited(key: String?) {}

            override fun onGeoQueryError(error: DatabaseError?) {}
        })
    }
}
