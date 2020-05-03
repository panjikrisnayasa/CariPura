package com.panjikrisnayasa.caripura.view.guest

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.viewmodel.guest.TempleDetailViewModel
import kotlinx.android.synthetic.main.activity_temple_detail.*

class TempleDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TEMPLE_ID = "temple_id"
    }

    private lateinit var mViewModel: TempleDetailViewModel
    private var mSamplePhoto = intArrayOf(
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temple_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            TempleDetailViewModel::class.java
        )

        val templeId = intent.getStringExtra(EXTRA_TEMPLE_ID)
        Log.d("hyperLoop", "temple detail $templeId")

        if (templeId != null) {
            mViewModel.setTempleDetail(templeId)
            mViewModel.getTempleDetail().observe(this, Observer { temple ->
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
                if (temple.prayerNeedsSellerChecked) {
                    linear_temple_detail_layout_3.visibility = View.VISIBLE
                    text_temple_detail_prayer_needs_seller.visibility = View.VISIBLE
                }
                if (temple.foodDrinkSellerChecked) {
                    linear_temple_detail_layout_3.visibility = View.VISIBLE
                    text_temple_detail_text_food_drink_seller.visibility = View.VISIBLE
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
