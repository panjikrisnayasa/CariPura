package com.panjikrisnayasa.caripura.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.model.Temple
import com.panjikrisnayasa.caripura.util.SharedPrefManager

class EditTempleFirstActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TEMPLE = "extra_temple"
    }

    private lateinit var mSharedPref: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_temple_first)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mSharedPref = SharedPrefManager.getInstance(this)

        if (mSharedPref.getRole() == "contributor") {
            title = getString(R.string.label_edit_temple_request)
        }

        val temple = intent.getParcelableExtra<Temple>(EXTRA_TEMPLE)
        if (temple != null)
            showTempleDetail(temple)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showTempleDetail(temple: Temple) {

    }
}