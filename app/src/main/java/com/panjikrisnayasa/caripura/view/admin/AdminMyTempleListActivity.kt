package com.panjikrisnayasa.caripura.view.admin

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.adapter.admin.AdminMyTempleListAdapter
import com.panjikrisnayasa.caripura.model.Temple
import kotlinx.android.synthetic.main.activity_admin_my_temple_list.*

class AdminMyTempleListActivity : AppCompatActivity() {

    private var mTempleList: ArrayList<Temple> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_my_temple_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showRecyclerView() {
        recycler_admin_my_temple_list?.visibility = View.VISIBLE
        text_admin_my_temple_list_no_data?.visibility = View.GONE
        val templeAdapter =
            AdminMyTempleListAdapter(
                mTempleList
            )
        recycler_admin_my_temple_list?.layoutManager = LinearLayoutManager(applicationContext)
        recycler_admin_my_temple_list?.adapter = templeAdapter
    }
}
