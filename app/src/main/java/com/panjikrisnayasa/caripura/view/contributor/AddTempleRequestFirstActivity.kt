package com.panjikrisnayasa.caripura.view.contributor

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.panjikrisnayasa.caripura.R
import kotlinx.android.synthetic.main.activity_add_edit_temple_first.*

class AddTempleRequestFirstActivity : AppCompatActivity(), OnMapReadyCallback,
    View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_temple_first)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
                val intent = Intent(this, AddTempleRequestSecondActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
