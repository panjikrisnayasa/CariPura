package com.panjikrisnayasa.caripura.view.guest


import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import com.panjikrisnayasa.caripura.viewmodel.guest.FindTempleViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_find_temple.*

/**
 * A simple [Fragment] subclass.
 */
class FindTempleFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mLastLocation: Location
    private lateinit var mViewModel: FindTempleViewModel
    private lateinit var mSharedPref: SharedPrefManager

    private var mGoogleMap: GoogleMap? = null

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

        mSharedPref = SharedPrefManager.getInstance(context)

        mViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FindTempleViewModel::class.java)

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
            mViewModel.setGeoQuery(mLastLocation, context, mGoogleMap)
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

    override fun onMapReady(p0: GoogleMap) {
        mGoogleMap = p0
        mGoogleMap?.setOnMarkerClickListener(this)

        setupMap()

        mGoogleMap?.isMyLocationEnabled = true
        mGoogleMap?.uiSettings?.isMyLocationButtonEnabled = false

        mFusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                mLastLocation = location
                mSharedPref.setLastLocation(
                    location.latitude.toString(),
                    location.longitude.toString()
                )
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

    internal class CustomInfoWindowAdapter(
        context: Context?,
        photoUrl: String,
        templeName: String
    ) :
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
                .centerCrop().into(
                    imageView,
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
}

