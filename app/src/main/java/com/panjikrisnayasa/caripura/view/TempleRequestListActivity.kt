package com.panjikrisnayasa.caripura.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.adapter.TempleRequestListViewPagerAdapter
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import kotlinx.android.synthetic.main.activity_temple_request_list.*

class TempleRequestListActivity : AppCompatActivity() {

    private lateinit var mSharedPref: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temple_request_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f

        mSharedPref = SharedPrefManager.getInstance(this)

        view_pager_temple_request_list.adapter =
            TempleRequestListViewPagerAdapter(
                supportFragmentManager,
                applicationContext
            )
        tab_temple_request_list.setupWithViewPager(view_pager_temple_request_list)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_history -> {
                val intent = Intent(applicationContext, TempleRequestHistoryActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}