package com.panjikrisnayasa.caripura.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.viewmodel.RequestedByViewModel
import kotlinx.android.synthetic.main.activity_requested_by.*

class RequestedByActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CONTRIBUTOR_ID = "contributor_id"
    }

    private lateinit var mViewModel: RequestedByViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requested_by)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(RequestedByViewModel::class.java)

        val contributorId = intent.getStringExtra(EXTRA_CONTRIBUTOR_ID)
        if (contributorId != null) {
            showUserData(contributorId)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showUserData(contributorId: String) {
        mViewModel.getUserData(contributorId).observe(this, Observer { user ->
            if (user != null) {
                text_input_edit_text_requested_by_full_name.setText(user.fullName)
                text_input_edit_text_requested_by_email.setText(user.email)
                text_input_edit_text_requested_by_phone_number.setText(user.phoneNumber)
                view_requested_by_background.visibility = View.GONE
                progress_requested_by.visibility = View.GONE
            }
        })
    }
}
