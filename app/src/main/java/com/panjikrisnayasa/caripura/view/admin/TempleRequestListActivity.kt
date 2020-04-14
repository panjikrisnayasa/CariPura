package com.panjikrisnayasa.caripura.view.admin

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.adapter.admin.TempleRequestListViewPagerAdapter
import kotlinx.android.synthetic.main.activity_temple_request_list.*

class TempleRequestListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temple_request_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f

        view_pager_temple_request_list.adapter =
            TempleRequestListViewPagerAdapter(
                supportFragmentManager,
                applicationContext
            )
        tab_temple_request_list.setupWithViewPager(view_pager_temple_request_list)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_temple_request_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_temple_request_list_history -> {
                val intent = Intent(applicationContext, TempleRequestHistoryActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
