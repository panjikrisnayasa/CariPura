package com.panjikrisnayasa.caripura.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.model.Temple
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import kotlinx.android.synthetic.main.activity_add_edit_temple_first.*

class AddTempleFirstActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {

    private lateinit var mSharedPref: SharedPrefManager
    private lateinit var mTemple: Temple

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_temple_first)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mSharedPref = SharedPrefManager.getInstance(this)

        if (mSharedPref.getRole() == "contributor") {
            title = getString(R.string.label_add_temple_request)
        }

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_add_edit_temple_first_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        button_add_edit_temple_first_next.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(p0: GoogleMap?) {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_add_edit_temple_first_next -> {
                val intent = Intent(this, AddTempleSecondActivity::class.java)
                intent.putExtra(AddTempleSecondActivity.EXTRA_TEMPLE, mTemple)
                startActivity(intent)
            }
        }
    }
}
