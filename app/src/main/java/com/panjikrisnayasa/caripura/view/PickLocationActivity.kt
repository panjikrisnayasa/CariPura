package com.panjikrisnayasa.caripura.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.panjikrisnayasa.caripura.R
import kotlinx.android.synthetic.main.activity_pick_location.*

class PickLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val REQUEST_PICK_LOCATION = 6
        const val RESULT_PICK_LOCATION = 601
        const val REQUEST_LOCATION = 2
    }

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mLastLocation: Location
    private var mTempleLat = ""
    private var mTempleLng = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_location)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_pick_location_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_LOCATION
            )
        }

        floating_action_button_pick_location_my_location.setOnClickListener {
            val currentLatLng = LatLng(mLastLocation.latitude, mLastLocation.longitude)
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f))
        }

        button_pick_location.setOnClickListener {
            if (mTempleLat == "" || mTempleLng == "") {
                Toast.makeText(
                    this,
                    getString(R.string.toast_message_pick_location),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val resultIntent = Intent()
                resultIntent.putExtra("temple_latitude", mTempleLat)
                resultIntent.putExtra("temple_longitude", mTempleLng)
                setResult(RESULT_PICK_LOCATION, resultIntent)
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(p0: GoogleMap?) {
        if (p0 != null)
            mGoogleMap = p0
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mGoogleMap.isMyLocationEnabled = true
        mGoogleMap.uiSettings.isMyLocationButtonEnabled = false

        val templeLat = intent.getStringExtra("temple_lat")
        val templeLng = intent.getStringExtra("temple_lng")
        if (templeLat != null && templeLng != null) {
            mTempleLat = templeLat
            mTempleLng = templeLng
            val templeLatLng = LatLng(mTempleLat.toDouble(), mTempleLng.toDouble())
            mGoogleMap.addMarker(MarkerOptions().position(templeLatLng))
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(templeLatLng, 18f))
        }

        mFusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                mLastLocation = it
                val currentLatLng = LatLng(it.latitude, it.longitude)
                if (mTempleLat == "" && mTempleLng == "")
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f))
            }
        }

        mGoogleMap.setOnMapClickListener {
            val markerOptions = MarkerOptions()
            markerOptions.position(it)
            mTempleLat = it.latitude.toString()
            mTempleLng = it.longitude.toString()
            mGoogleMap.clear()
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(it))
            mGoogleMap.addMarker(markerOptions)
        }
    }
}
