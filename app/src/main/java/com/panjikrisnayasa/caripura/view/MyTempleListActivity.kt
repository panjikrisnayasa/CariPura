package com.panjikrisnayasa.caripura.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.adapter.MyTempleListAdapter
import com.panjikrisnayasa.caripura.adapter.MyTempleListViewPagerAdapter
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import com.panjikrisnayasa.caripura.viewmodel.TempleListViewModel
import kotlinx.android.synthetic.main.activity_my_temple_list.*

class MyTempleListActivity : AppCompatActivity() {

    private lateinit var mSharedPref: SharedPrefManager
    private lateinit var mAdapter: MyTempleListAdapter

    //changed
    private lateinit var mViewModel: TempleListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_temple_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f

        mViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(TempleListViewModel::class.java)

        mSharedPref = SharedPrefManager.getInstance(this)
        if (mSharedPref.getRole() == "contributor") {
            progress_my_temple_list.visibility = View.GONE
            text_my_temple_list_no_data.visibility = View.GONE
            recycler_my_temple_list.visibility = View.GONE
            floating_action_button_my_temple_list_add_temple.hide()
            view_pager_my_temple_list.adapter =
                MyTempleListViewPagerAdapter(
                    supportFragmentManager,
                    applicationContext
                )
            tab_my_temple_list.setupWithViewPager(view_pager_my_temple_list)
        } else {
            supportActionBar?.elevation = 8f
            view_pager_my_temple_list.visibility = View.GONE
            tab_my_temple_list.visibility = View.GONE
            floating_action_button_my_temple_list_add_temple.setOnClickListener {
                val intent = Intent(this, AddTempleFirstActivity::class.java)
                startActivityForResult(intent, AddTempleFirstActivity.REQUEST_ADD)
            }

            showRecyclerView()

            mViewModel.getTempleList().observe(this, Observer { templeList ->
                progress_my_temple_list.visibility = View.GONE
                if (templeList != null) {
                    text_my_temple_list_no_data.visibility = View.GONE
                    mAdapter.setData(templeList)
                } else {
                    text_my_temple_list_no_data.visibility = View.VISIBLE
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (requestCode == MyTempleDetailActivity.REQUEST_UPDATE_ADMIN) {
                if (resultCode == MyTempleDetailActivity.RESULT_DELETE_ADMIN) {
                    val position = data.getIntExtra(MyTempleDetailActivity.EXTRA_POSITION, 0)
                    mAdapter.removeItem(position)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            if (mSharedPref.getRole() == "admin") {
                menu.findItem(R.id.menu_history).isVisible = false
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_history -> {
                val historyIntent = Intent(this, TempleRequestHistoryActivity::class.java)
                startActivity(historyIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showRecyclerView() {
        mAdapter = MyTempleListAdapter(null)
        recycler_my_temple_list?.layoutManager = LinearLayoutManager(this)
        recycler_my_temple_list?.adapter = mAdapter
    }
}
