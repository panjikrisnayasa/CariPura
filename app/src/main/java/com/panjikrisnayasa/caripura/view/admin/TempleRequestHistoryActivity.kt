package com.panjikrisnayasa.caripura.view.admin

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.adapter.admin.TempleRequestHistoryViewPagerAdapter
import kotlinx.android.synthetic.main.activity_temple_request_history.*

class TempleRequestHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temple_request_history)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f

        view_pager_temple_request_history.adapter =
            TempleRequestHistoryViewPagerAdapter(
                supportFragmentManager,
                applicationContext
            )
        tab_temple_request_history.setupWithViewPager(view_pager_temple_request_history)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
