package com.panjikrisnayasa.caripura.view

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.model.Temple
import com.panjikrisnayasa.caripura.util.SharedPrefLocationManager
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import com.panjikrisnayasa.caripura.viewmodel.TempleDetailViewModel
import kotlinx.android.synthetic.main.activity_temple_detail.*
import org.json.JSONException
import org.json.JSONObject

class MyTempleDetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_TEMPLE = "temple"
        const val EXTRA_TEMPLE_ID = "temple_id"
        const val EXTRA_POSITION = "position"
        const val REQUEST_UPDATE_ADMIN = 1
        const val RESULT_DELETE_ADMIN = 101
        const val REQUEST_UPDATE_CONTRIBUTOR = 2
        const val RESULT_UPDATE_CONTRIBUTOR = 201
    }

    private lateinit var mSharedPref: SharedPrefManager
    private lateinit var mSharedPrefLocation: SharedPrefLocationManager
    private lateinit var mViewModel: TempleDetailViewModel
    private var mSamplePhoto = intArrayOf(
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background
    )
    private lateinit var mTemple: Temple

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temple_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mSharedPref = SharedPrefManager.getInstance(applicationContext)
        mSharedPrefLocation = SharedPrefLocationManager.getInstance(applicationContext)

        mViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            TempleDetailViewModel::class.java
        )

        button_temple_detail_call.setOnClickListener(this)
        button_temple_detail_route.setOnClickListener(this)

        if (mSharedPref.getRole() == "contributor") {
            val temple = intent.getParcelableExtra<Temple>(EXTRA_TEMPLE)
            if (temple != null) {
                showTempleDetail(temple)
            }
        } else {
            val templeId = intent.getStringExtra(EXTRA_TEMPLE_ID)
            if (templeId != null)
                mViewModel.getTempleDetail(templeId).observe(this, Observer { temple ->
                    if (temple != null)
                        showTempleDetail(temple)
                })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_my_temple_detail_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (requestCode == DeleteTempleActivity.REQUEST_DELETE) {
                if (resultCode == DeleteTempleActivity.RESULT_DELETE) {
                    val resultIntent = Intent()
                    setResult(RESULT_UPDATE_CONTRIBUTOR, resultIntent)
                    finish()
                }
            } else if (requestCode == EditTempleFirstActivity.REQUEST_EDIT) {
                if (resultCode == EditTempleFirstActivity.RESULT_EDIT_1) {
                    val resultIntent = Intent()
                    setResult(RESULT_UPDATE_CONTRIBUTOR, resultIntent)
                    finish()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_my_temple_detail_options_edit -> {
                if (mTemple.requestStatus == "waiting") {
                    Toast.makeText(
                        this,
                        getString(R.string.toast_message_waiting_admin_approval),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val editIntent = Intent(this, EditTempleFirstActivity::class.java)
                    editIntent.putExtra(EditTempleFirstActivity.EXTRA_OLD_TEMPLE, mTemple)
                    startActivityForResult(editIntent, EditTempleFirstActivity.REQUEST_EDIT)
                }
            }
            R.id.menu_my_temple_detail_options_delete -> {
                if (mTemple.requestStatus == "waiting") {
                    Toast.makeText(
                        this,
                        getString(R.string.toast_message_waiting_admin_approval),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (mSharedPref.getRole() == "contributor") {
                        val deleteIntent = Intent(this, DeleteTempleActivity::class.java)
                        deleteIntent.putExtra(DeleteTempleActivity.EXTRA_TEMPLE, mTemple)
                        startActivityForResult(deleteIntent, DeleteTempleActivity.REQUEST_DELETE)
                    } else {
                        val alertBuilder = AlertDialog.Builder(this)
                        alertBuilder.setTitle(getString(R.string.dialog_delete_title))
                        alertBuilder.setMessage(getString(R.string.dialog_delete_message))
                        alertBuilder.setPositiveButton(getString(R.string.dialog_delete_positive_button)) { _, _ ->
                        }
                        alertBuilder.setNegativeButton(getString(R.string.dialog_delete_negative_button)) { _, _ ->
                            val resultIntent = Intent()
                            setResult(RESULT_DELETE_ADMIN, resultIntent)
                            mViewModel.deleteTemple(mTemple).observe(this, Observer {
                                if (it != null) {
                                    if (mTemple.contributorId != "") {
                                        val topic = "/topics/approval_" + mTemple.contributorId
                                        val notification = JSONObject()
                                        val notificationBody = JSONObject()
                                        try {
                                            notificationBody.put(
                                                "title",
                                                "Hapus pura oleh Admin"
                                            )
                                            notificationBody.put(
                                                "message",
                                                String.format(
                                                    "${mTemple.name} milik Anda telah dihapus oleh Admin"
                                                )
                                            )
                                            notificationBody.put(
                                                "requestType",
                                                "history_delete_request"
                                            )
                                            notificationBody.put(
                                                "contributorId",
                                                mTemple.contributorId
                                            )
                                            notificationBody.put("templeId", it)
                                            notification.put("to", topic)
                                            notification.put("data", notificationBody)
                                        } catch (e: JSONException) {
                                            e.message
                                        }
                                        mViewModel.sendNotification(
                                            notification,
                                            applicationContext
                                        )
                                    }
                                    Toast.makeText(
                                        this,
                                        getString(R.string.toast_message_temple_deleted),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                }
                            })
                        }
                        val alertDialog = alertBuilder.create()
                        alertDialog.show()
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                            .setTextColor(
                                ContextCompat.getColor(
                                    applicationContext,
                                    R.color.colorNegativeButton
                                )
                            )
                        val message: TextView = alertDialog.findViewById(android.R.id.message)
                        message.typeface =
                            Typeface.createFromAsset(applicationContext.assets, "gotham_book.ttf")
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_temple_detail_call -> {
                val callIntent =
                    Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mTemple.caretakerPhone))
                startActivity(callIntent)
            }
            R.id.button_temple_detail_route -> {
                val routeIntent = Intent(this, RouteToTempleActivity::class.java)
                routeIntent.putExtra(RouteToTempleActivity.EXTRA_TEMPLE, mTemple)
                startActivity(routeIntent)
            }
        }
    }

    private fun showTempleDetail(temple: Temple) {
        mTemple = temple
        mViewModel.getDistanceDuration(
            mSharedPrefLocation.getLastLat(),
            mSharedPrefLocation.getLastLng(),
            temple.lat,
            temple.lng
        ).observe(this, Observer { distanceDuration ->
            text_temple_detail_distance.text = distanceDuration[0]
            text_temple_detail_duration.text = distanceDuration[1]

            carousel_temple_detail_photo.setImageListener { _, imageView ->
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(this).load(temple.photo).into(imageView)
            }
            carousel_temple_detail_photo.pageCount = mSamplePhoto.count()
            text_temple_detail_name.text = temple.name
            text_temple_detail_address.text = String.format(
                "%s, %s, %s",
                temple.address,
                temple.villageOffice,
                temple.subDistrict
            )
            text_temple_detail_caretaker_name.text = temple.caretakerName
            text_temple_detail_caretaker_phone.text = temple.caretakerPhone
            text_temple_detail_feast_day.text = temple.feastDay
            text_temple_detail_feast_day_start.text = temple.feastDayPrayerStart
            text_temple_detail_feast_day_end.text = temple.feastDayPrayerEnd
            text_temple_detail_full_moon_prayer_start.text = temple.fullMoonPrayerStart
            text_temple_detail_full_moon_prayer_end.text = temple.fullMoonPrayerEnd
            text_temple_detail_dead_moon_prayer_start.text = temple.deadMoonPrayerStart
            text_temple_detail_dead_moon_prayer_end.text = temple.deadMoonPrayerEnd
            if (temple.galunganPrayerStart != "") {
                linear_temple_detail_galungan.visibility = View.VISIBLE
                text_temple_detail_galungan_prayer_start.text = temple.galunganPrayerStart
                text_temple_detail_galungan_prayer_end.text = temple.galunganPrayerEnd
            }
            if (temple.kuninganPrayerStart != "") {
                linear_temple_detail_kuningan.visibility = View.VISIBLE
                text_temple_detail_kuningan_prayer_start.text = temple.kuninganPrayerStart
                text_temple_detail_kuningan_prayer_end.text = temple.kuninganPrayerEnd
            }
            if (temple.saraswatiPrayerStart != "") {
                linear_temple_detail_saraswati.visibility = View.VISIBLE
                text_temple_detail_saraswati_prayer_start.text = temple.saraswatiPrayerStart
                text_temple_detail_saraswati_prayer_end.text = temple.saraswatiPrayerEnd
            }
            if (temple.pagerwesiPrayerStart != "") {
                linear_temple_detail_pagerwesi.visibility = View.VISIBLE
                text_temple_detail_pagerwesi_prayer_start.text = temple.pagerwesiPrayerStart
                text_temple_detail_pagerwesi_prayer_end.text = temple.pagerwesiPrayerEnd
            }
            if (temple.pagerwesiPrayerStart != "") {
                linear_temple_detail_pagerwesi.visibility = View.VISIBLE
                text_temple_detail_pagerwesi_prayer_start.text = temple.pagerwesiPrayerStart
                text_temple_detail_pagerwesi_prayer_end.text = temple.pagerwesiPrayerEnd
            }
            if (temple.siwaratriPrayerStart != "") {
                linear_temple_detail_siwaratri.visibility = View.VISIBLE
                text_temple_detail_siwaratri_prayer_start.text = temple.siwaratriPrayerStart
                text_temple_detail_siwaratri_prayer_end.text = temple.siwaratriPrayerEnd
            }
            if (temple.melukatPrayerStart != "") {
                linear_temple_detail_melukat.visibility = View.VISIBLE
                text_temple_detail_melukat_information.visibility = View.VISIBLE
                text_temple_detail_melukat_prayer_start.text = temple.melukatPrayerStart
                text_temple_detail_melukat_prayer_end.text = temple.melukatPrayerEnd
                text_temple_detail_melukat_information.text = temple.melukatInformation
            }
            if (!temple.prayerEquipmentSellerChecked) {
                text_temple_detail_prayer_equipment_seller.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_close_grey_14dp,
                    0,
                    0,
                    0
                )
            }
            if (!temple.foodDrinkSellerChecked) {
                text_temple_detail_food_drink_seller.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_close_grey_14dp,
                    0,
                    0,
                    0
                )
            }
            view_temple_detail_background.visibility = View.GONE
            progress_temple_detail.visibility = View.GONE
        })
    }
}
