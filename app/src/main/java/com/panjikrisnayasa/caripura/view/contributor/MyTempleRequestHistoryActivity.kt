package com.panjikrisnayasa.caripura.view.contributor

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.adapter.contributor.MyTempleRequestHistoryViewPagerAdapter
import kotlinx.android.synthetic.main.activity_my_temple_request_history.*

class MyTempleRequestHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_temple_request_history)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f

        view_pager_my_temple_request_history.adapter =
            MyTempleRequestHistoryViewPagerAdapter(
                supportFragmentManager,
                applicationContext
            )
        tab_my_temple_request_history.setupWithViewPager(view_pager_my_temple_request_history)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
