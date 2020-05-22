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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.model.Temple
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import com.panjikrisnayasa.caripura.viewmodel.TempleDetailViewModel
import kotlinx.android.synthetic.main.activity_my_temple_detail_waiting.*

class MyTempleDetailWaitingActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_TEMPLE = "temple"
    }

    private lateinit var mSharedPref: SharedPrefManager
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
        setContentView(R.layout.activity_my_temple_detail_waiting)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mSharedPref = SharedPrefManager.getInstance(applicationContext)

        mViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            TempleDetailViewModel::class.java
        )

        button_my_temple_detail_waiting_call.setOnClickListener(this)
        button_my_temple_detail_waiting_route.setOnClickListener(this)

        val temple = intent.getParcelableExtra<Temple>(MyTempleDetailActivity.EXTRA_TEMPLE)
        if (temple != null) {
            showTempleDetail(temple)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cancel, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_cancel -> {
                val alertBuilder = AlertDialog.Builder(this)
                when (mTemple.requestType) {
                    "add" -> {
                        alertBuilder.setTitle(getString(R.string.dialog_cancel_title_add))
                        alertBuilder.setMessage(getString(R.string.dialog_cancel_message_add))
                    }
                    "edit" -> {
                        alertBuilder.setTitle(getString(R.string.dialog_cancel_title_edit))
                        alertBuilder.setMessage(getString(R.string.dialog_cancel_message_edit))
                    }
                    else -> {
                        alertBuilder.setTitle(getString(R.string.dialog_cancel_title_delete))
                        alertBuilder.setMessage(getString(R.string.dialog_cancel_message_delete))
                    }
                }
                alertBuilder.setPositiveButton(getString(R.string.dialog_cancel_positive_button)) { _, _ ->

                }
                alertBuilder.setNegativeButton(getString(R.string.dialog_cancel_negative_button)) { _, _ ->

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
            mSharedPref.getLastLat(),
            mSharedPref.getLastLng(),
            temple.lat,
            temple.lng
        ).observe(this, Observer { distanceDuration ->
            text_my_temple_detail_waiting_distance.text = distanceDuration[0]
            text_my_temple_detail_waiting_duration.text = distanceDuration[1]

            when (temple.requestType) {
                "add" -> {
                    text_my_temple_detail_waiting_label.text =
                        getString(R.string.item_label_request_add_temple)
                    text_my_temple_detail_waiting_label.background = getDrawable(R.color.colorGreen)
                }
                "edit" -> {
                    text_my_temple_detail_waiting_label.text =
                        getString(R.string.item_label_request_edit_temple)
                    text_my_temple_detail_waiting_label.background = getDrawable(R.color.colorOrange)
                }
                else -> {
                    text_my_temple_detail_waiting_label.text =
                        getString(R.string.item_label_request_delete_temple)
                    text_my_temple_detail_waiting_label.background = getDrawable(R.color.colorRed)
                }
            }

            carousel_my_temple_detail_waiting_photo.setImageListener { _, imageView ->
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(this).load(temple.photo).into(imageView)
            }
            carousel_my_temple_detail_waiting_photo.pageCount = mSamplePhoto.count()
            text_my_temple_detail_waiting_name.text = temple.name
            text_my_temple_detail_waiting_address.text = String.format(
                "%s, %s, %s",
                temple.address,
                temple.villageOffice,
                temple.subDistrict
            )
            text_my_temple_detail_waiting_caretaker_name.text = temple.caretakerName
            text_my_temple_detail_waiting_caretaker_phone.text = temple.caretakerPhone
            text_my_temple_detail_waiting_feast_day.text = temple.feastDay
            text_my_temple_detail_waiting_feast_day_start.text = temple.feastDayPrayerStart
            text_my_temple_detail_waiting_feast_day_end.text = temple.feastDayPrayerEnd
            text_my_temple_detail_waiting_full_moon_prayer_start.text = temple.fullMoonPrayerStart
            text_my_temple_detail_waiting_full_moon_prayer_end.text = temple.fullMoonPrayerEnd
            text_my_temple_detail_waiting_dead_moon_prayer_start.text = temple.deadMoonPrayerStart
            text_my_temple_detail_waiting_dead_moon_prayer_end.text = temple.deadMoonPrayerEnd
            edit_my_temple_detail_waiting_note_for_admin.setText(temple.contributorNote)
            if (temple.galunganPrayerStart != "") {
                linear_my_temple_detail_waiting_galungan.visibility = View.VISIBLE
                text_my_temple_detail_waiting_galungan_prayer_start.text =
                    temple.galunganPrayerStart
                text_my_temple_detail_waiting_galungan_prayer_end.text = temple.galunganPrayerEnd
            }
            if (temple.kuninganPrayerStart != "") {
                linear_my_temple_detail_waiting_kuningan.visibility = View.VISIBLE
                text_my_temple_detail_waiting_kuningan_prayer_start.text =
                    temple.kuninganPrayerStart
                text_my_temple_detail_waiting_kuningan_prayer_end.text = temple.kuninganPrayerEnd
            }
            if (temple.saraswatiPrayerStart != "") {
                linear_my_temple_detail_waiting_saraswati.visibility = View.VISIBLE
                text_my_temple_detail_waiting_saraswati_prayer_start.text =
                    temple.saraswatiPrayerStart
                text_my_temple_detail_waiting_saraswati_prayer_end.text = temple.saraswatiPrayerEnd
            }
            if (temple.pagerwesiPrayerStart != "") {
                linear_my_temple_detail_waiting_pagerwesi.visibility = View.VISIBLE
                text_my_temple_detail_waiting_pagerwesi_prayer_start.text =
                    temple.pagerwesiPrayerStart
                text_my_temple_detail_waiting_pagerwesi_prayer_end.text = temple.pagerwesiPrayerEnd
            }
            if (temple.pagerwesiPrayerStart != "") {
                linear_my_temple_detail_waiting_pagerwesi.visibility = View.VISIBLE
                text_my_temple_detail_waiting_pagerwesi_prayer_start.text =
                    temple.pagerwesiPrayerStart
                text_my_temple_detail_waiting_pagerwesi_prayer_end.text = temple.pagerwesiPrayerEnd
            }
            if (temple.siwaratriPrayerStart != "") {
                linear_my_temple_detail_waiting_siwaratri.visibility = View.VISIBLE
                text_my_temple_detail_waiting_siwaratri_prayer_start.text =
                    temple.siwaratriPrayerStart
                text_my_temple_detail_waiting_siwaratri_prayer_end.text = temple.siwaratriPrayerEnd
            }
            if (temple.melukatPrayerStart != "") {
                linear_my_temple_detail_waiting_melukat.visibility = View.VISIBLE
                text_my_temple_detail_waiting_melukat_information.visibility = View.VISIBLE
                text_my_temple_detail_waiting_melukat_prayer_start.text = temple.melukatPrayerStart
                text_my_temple_detail_waiting_melukat_prayer_end.text = temple.melukatPrayerEnd
                text_my_temple_detail_waiting_melukat_information.text = temple.melukatInformation
            }
            if (!temple.prayerEquipmentSellerChecked) {
                text_my_temple_detail_waiting_prayer_equipment_seller.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_close_grey_14dp,
                    0,
                    0,
                    0
                )
            }
            if (!temple.foodDrinkSellerChecked) {
                text_my_temple_detail_waiting_food_drink_seller.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_close_grey_14dp,
                    0,
                    0,
                    0
                )
            }
            view_my_temple_detail_waiting_background.visibility = View.GONE
            progress_my_temple_detail_waiting.visibility = View.GONE
        })
    }
}
