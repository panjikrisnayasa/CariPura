package com.panjikrisnayasa.caripura.view

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.model.Temple
import com.panjikrisnayasa.caripura.viewmodel.FindTempleViewModel
import kotlinx.android.synthetic.main.activity_route_to_temple.*

class RouteToTempleActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    companion object {
        const val EXTRA_TEMPLE = "temple"
    }

    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mLastLocation: Location
    private lateinit var mViewModel: FindTempleViewModel
    private var mGoogleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_to_temple)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FindTempleViewModel::class.java)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_route_to_temple_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        floating_action_button_route_to_temple_my_location.setOnClickListener {
            val latLng = LatLng(mLastLocation.latitude, mLastLocation.longitude)
            mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(p0: GoogleMap?) {
        mGoogleMap = p0
        mGoogleMap?.setOnMarkerClickListener(this)

        setupMap()

        mGoogleMap?.isMyLocationEnabled = true
        mGoogleMap?.uiSettings?.isMyLocationButtonEnabled = false

        val temple = intent.getParcelableExtra<Temple>(EXTRA_TEMPLE)

        mFusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                mLastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
                if (temple != null) {
                    val latLng =
                        LatLng(
                            temple.lat.toDouble(),
                            temple.lng.toDouble()
                        )
                    mViewModel.setDirectionApi(
                        location.latitude.toString(),
                        location.longitude.toString(),
                        temple.lat,
                        temple.lng
                    )
                    mViewModel.getPath().observe(this, Observer { path ->
                        FindTempleFragment.drawPolyline(mGoogleMap, path)
                    })
                    mViewModel.getDistanceDuration()
                        .observe(this, Observer { distanceDuration ->
                            floating_action_button_route_to_temple_my_location.show()
                            card_route_to_temple_bottom.visibility = View.VISIBLE
                            showTemple(temple, latLng, distanceDuration)
                        })
                }
            }
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean = false

    private fun setupMap() {
        val tContext = applicationContext
        if (tContext != null) {
            if (ActivityCompat.checkSelfPermission(
                    tContext,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    FindTempleFragment.LOCATION_PERMISSION_REQUEST_CODE
                )
                return
            }
        }
    }

    private fun showTemple(temple: Temple, latLng: LatLng, distanceDuration: ArrayList<String>) {
        mGoogleMap?.setInfoWindowAdapter(
            FindTempleFragment.CustomInfoWindowAdapter(
                this,
                temple.photo,
                temple.name
            )
        )
        mGoogleMap?.addMarker(
            MarkerOptions().position(latLng)
                .title(temple.name)
        )?.showInfoWindow()
        mGoogleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng,
                18f
            )
        )
        text_route_to_temple_name.text = temple.name
        text_route_to_temple_distance.text = distanceDuration[0]
        text_route_to_temple_duration.text = distanceDuration[1]
    }
}
