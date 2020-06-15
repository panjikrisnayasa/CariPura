package com.panjikrisnayasa.caripura.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.model.Temple
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import com.panjikrisnayasa.caripura.viewmodel.DeleteTempleViewModel
import kotlinx.android.synthetic.main.activity_delete_temple.*
import org.json.JSONException
import org.json.JSONObject

class DeleteTempleActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_TEMPLE = "extra_temple"
        const val REQUEST_DELETE = 4
        const val RESULT_DELETE = 401
    }

    private lateinit var mViewModel: DeleteTempleViewModel
    private lateinit var mSharedPref: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_temple)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mSharedPref = SharedPrefManager.getInstance(this)
        mViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DeleteTempleViewModel::class.java
        )

        button_delete_temple_delete.setOnClickListener(this)
        button_delete_temple_cancel.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_delete_temple_delete -> {
                val noteForAdmin = edit_delete_temple_note_for_admin.text.toString()
                if (noteForAdmin.isBlank())
                    edit_delete_temple_note_for_admin.error =
                        getString(R.string.error_message_fill_this_field)
                else {
                    val resultIntent = Intent()
                    val temple = intent.getParcelableExtra<Temple>(EXTRA_TEMPLE)
                    if (temple != null) {
                        temple.contributorNote = noteForAdmin
                        mViewModel.deleteTempleRequest(temple)
                        Toast.makeText(
                            this,
                            getString(R.string.toast_message_delete_temple_requested),
                            Toast.LENGTH_SHORT
                        ).show()
                        val topic = "/topics/request"
                        val notification = JSONObject()
                        val notificationBody = JSONObject()
                        try {
                            notificationBody.put(
                                "title", "Pengajuan Hapus Pura"
                            )
                            notificationBody.put(
                                "message",
                                "${temple.name} diajukan untuk dihapus"
                            )
                            notificationBody.put("requestType", "delete_request")
                            notificationBody.put(
                                "contributorId",
                                temple.contributorId
                            )
                            notificationBody.put("templeId", temple.id)
                            notification.put("to", topic)
                            notification.put("data", notificationBody)
                        } catch (e: JSONException) {
                            e.message
                        }
                        mViewModel.sendNotification(notification, applicationContext)
                        setResult(RESULT_DELETE, resultIntent)
                        finish()
                    }
                }
            }
            R.id.button_delete_temple_cancel -> {
                finish()
            }
        }
    }
}
