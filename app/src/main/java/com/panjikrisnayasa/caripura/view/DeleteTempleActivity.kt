package com.panjikrisnayasa.caripura.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.model.Temple
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import com.panjikrisnayasa.caripura.viewmodel.DeleteTempleViewModel

class DeleteTempleActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TEMPLE = "extra_temple"
    }

    private lateinit var mViewModel: DeleteTempleViewModel
    private lateinit var mSharedPref: SharedPrefManager
    private lateinit var mTemple: Temple

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_temple_request)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mSharedPref = SharedPrefManager.getInstance(this)
        mViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DeleteTempleViewModel::class.java
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
