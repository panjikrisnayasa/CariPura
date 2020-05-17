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
import kotlinx.android.synthetic.main.activity_temple_detail.*

class MyTempleDetailActivity : AppCompatActivity(), View.OnClickListener {

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
        setContentView(R.layout.activity_temple_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mSharedPref = SharedPrefManager.getInstance(applicationContext)

        mViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            TempleDetailViewModel::class.java
        )

        button_temple_detail_call.setOnClickListener(this)
        button_temple_detail_route.setOnClickListener(this)

        val temple = intent.getParcelableExtra<Temple>(EXTRA_TEMPLE)
        if (temple != null) {
            showTempleDetail(temple)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_my_temple_detail_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        //if temple waiting approval show cancel menu and hide other menus
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_my_temple_detail_options_edit -> {
                val editIntent = Intent(this, EditTempleFirstActivity::class.java)
                editIntent.putExtra(EditTempleFirstActivity.EXTRA_TEMPLE, mTemple)
                startActivity(editIntent)
            }
            R.id.menu_my_temple_detail_options_delete -> {
                if (mSharedPref.getRole() == "contributor") {
                    val deleteIntent = Intent(this, DeleteTempleActivity::class.java)
                    deleteIntent.putExtra(DeleteTempleActivity.EXTRA_TEMPLE, mTemple)
                    startActivity(deleteIntent)
                } else {
                    val alertBuilder = AlertDialog.Builder(this)
                    alertBuilder.setTitle(getString(R.string.dialog_delete_title))
                    alertBuilder.setMessage(getString(R.string.dialog_delete_message))
                    alertBuilder.setPositiveButton(getString(R.string.dialog_delete_positive_button)) { _, _ ->

                    }
                    alertBuilder.setNegativeButton(getString(R.string.dialog_delete_negative_button)) { _, _ ->

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