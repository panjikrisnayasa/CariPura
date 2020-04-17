package com.panjikrisnayasa.caripura.view.contributor

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.adapter.contributor.ContributorMyTempleListViewPagerAdapter
import kotlinx.android.synthetic.main.activity_contributor_my_temple_list.*

class ContributorMyTempleListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contributor_my_temple_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f

        view_pager_contributor_my_temple_list.adapter =
            ContributorMyTempleListViewPagerAdapter(
                supportFragmentManager,
                applicationContext
            )
        tab_contributor_my_temple_list.setupWithViewPager(view_pager_contributor_my_temple_list)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_history -> {
//                val intent = Intent(applicationContext, MyTempleRequestHistoryActivity::class.java)
//                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
