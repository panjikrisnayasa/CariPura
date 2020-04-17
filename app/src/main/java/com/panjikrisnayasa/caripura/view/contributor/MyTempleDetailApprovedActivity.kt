package com.panjikrisnayasa.caripura.view.contributor

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.panjikrisnayasa.caripura.R

class MyTempleDetailApprovedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temple_detail)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_my_temple_detail_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_my_temple_detail_options_edit -> {
                val editIntent = Intent(this, EditTempleRequestFirstActivity::class.java)
                startActivity(editIntent)
            }
            R.id.menu_my_temple_detail_options_delete -> {
                val deleteIntent = Intent(this, DeleteTempleRequestActivity::class.java)
                startActivity(deleteIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
