package com.panjikrisnayasa.caripura.view.guest


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.firebase.geofire.util.GeoUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.model.Temple
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
        const val LOCATION_PERMISSION_REQUEST_CODE = 1

        fun drawPolyline(googleMap: GoogleMap?, path: MutableList<List<LatLng>>) {
            for (i in 0 until path.size) {
                when (i) {
                    0 -> {
                        googleMap?.addPolyline(
                            PolylineOptions().addAll(path[i]).width(16f)
                                .startCap(
                                    CustomCap(
                                        BitmapDescriptorFactory.fromResource(
                                            R.drawable.ic_route_start_end_white_8dp
                                        )
                                    )
                                )
                                .endCap(RoundCap())
                                .jointType(JointType.ROUND)
                                .color(0xFF4285F4.toInt()).geodesic(true)
                        )
                    }
                    path.size - 1 -> {
                        googleMap?.addPolyline(
                            PolylineOptions().addAll(path[i]).width(16f)
                                .startCap(RoundCap())
                                .endCap(
                                    CustomCap(
                                        BitmapDescriptorFactory.fromResource(
                                            R.drawable.ic_route_start_end_white_8dp
                                        )
                                    )
                                )
                                .jointType(JointType.ROUND)
                                .color(0xFF4285F4.toInt()).geodesic(true)
                        )
                    }
                    else -> {
                        googleMap?.addPolyline(
                            PolylineOptions().addAll(path[i]).width(16f)
                                .startCap(RoundCap())
                                .endCap(RoundCap())
                                .jointType(JointType.ROUND)
                                .color(0xFF4285F4.toInt()).geodesic(true)
                        )
                    }
                }
            }
        }
    }

    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mLastLocation: Location
    private lateinit var mViewModel: FindTempleViewModel
    private lateinit var mSharedPref: SharedPrefManager
    private lateinit var mTempleId: String

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

        button_find_temple_find_the_closest_temple.setOnClickListener {
            progress_find_temple.visibility = View.VISIBLE
            mViewModel.getAllTemple(mLastLocation)
                .observe(this, Observer { templeList ->
                    if (templeList == null) {
                        Toast.makeText(
                            context,
                            getString(R.string.find_temple_toast_no_temple),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val lastLat = mLastLocation.latitude.toString()
                        val lastLng = mLastLocation.longitude.toString()

                        val closestTemple = getClosestTemple(templeList)
                        if (closestTemple != null) {
                            mTempleId = closestTemple.id
                            val latLng =
                                LatLng(
                                    closestTemple.lat.toDouble(),
                                    closestTemple.lng.toDouble()
                                )
                            mViewModel.setDirectionApi(
                                lastLat,
                                lastLng,
                                closestTemple.lat,
                                closestTemple.lng
                            )
                            mViewModel.getPath().observe(this, Observer { path ->
                                drawPolyline(mGoogleMap, path)
                            })
                            mViewModel.getDistanceDuration()
                                .observe(this, Observer { distanceDuration ->
                                    showTemple(closestTemple, latLng, distanceDuration)
                                })
                        }
                    }
                    progress_find_temple.visibility = View.GONE
                })
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
        mGoogleMap?.setOnInfoWindowClickListener {
            val intent = Intent(context, TempleDetailActivity::class.java)
            intent.putExtra(TempleDetailActivity.EXTRA_TEMPLE_ID, mTempleId)
            startActivity(intent)
        }

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

    private fun getClosestTemple(templeList: ArrayList<Temple>): Temple? {
        var closestDistance = 0.0
        var closestTemple: Temple? = null
        for (i in 0 until templeList.size) {
            val distance = GeoUtils.distance(
                mLastLocation.latitude,
                mLastLocation.longitude,
                templeList[i].lat.toDouble(),
                templeList[i].lng.toDouble()
            )
            if (i == 0) {
                closestDistance = distance
                closestTemple = templeList[i]
            }
            if (distance < closestDistance) {
                closestDistance = distance
                closestTemple = templeList[i]
            }
        }
        return closestTemple
    }

    private fun showTemple(
        templeList: Temple,
        latLng: LatLng,
        distanceDuration: ArrayList<String>
    ) {
        mGoogleMap?.setInfoWindowAdapter(
            CustomInfoWindowAdapter(
                context,
                templeList.photo,
                templeList.name
            )
        )
        mGoogleMap?.addMarker(
            MarkerOptions().position(latLng)
                .title(templeList.name)
        )?.showInfoWindow()
        mGoogleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng,
                18f
            )
        )
        text_find_temple_name.text = templeList.name
        text_find_temple_distance.text = distanceDuration[0]
        text_find_temple_duration.text = distanceDuration[1]

        val params =
            floating_action_button_find_temple_my_location.layoutParams as ConstraintLayout.LayoutParams
        params.bottomToTop = card_find_temple_bottom.id
        floating_action_button_find_temple_my_location.requestLayout()

        button_find_temple_find_the_closest_temple.visibility =
            View.GONE
        card_find_temple_bottom.visibility = View.VISIBLE
    }

    internal class CustomInfoWindowAdapter(
        context: Context?,
        photoUrl: String,
        templeName: String
    ) : GoogleMap.InfoWindowAdapter {

        companion object {
            class MarkerCallback(private var mMarker: Marker?) : Callback {

                override fun onSuccess() {
                    val tMarker = mMarker ?: return
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

        private var mPhotoUrl: String = photoUrl
        private var mTempleName: String = templeName
        private var viewGroup: ViewGroup? = null
        private var layoutInflater =
            context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        private var view = layoutInflater.inflate(R.layout.info_window_custom, viewGroup, false)

        override fun getInfoContents(p0: Marker?): View? {
            val imageView = view.findViewById<ImageView>(R.id.image_info_window_custom)
            val textViewTempleName =
                view.findViewById<TextView>(R.id.text_info_window_custom_temple_name)
            Picasso.get().load(mPhotoUrl)
                .into(
                    imageView,
                    MarkerCallback(
                        p0
                    )
                )
            textViewTempleName.text = mTempleName
            return view
        }

        override fun getInfoWindow(p0: Marker?): View? {
            return null
        }
    }
}

