package com.panjikrisnayasa.caripura.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.model.Temple
import com.panjikrisnayasa.caripura.util.SharedPrefLocationManager
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import com.panjikrisnayasa.caripura.viewmodel.TempleDetailViewModel
import kotlinx.android.synthetic.main.activity_temple_request_history_detail_contributor.*

class TempleRequestHistoryDetailContributorActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_TEMPLE = "temple"
        const val EXTRA_REQUEST_TYPE = "request_type"
        const val EXTRA_CONTRIBUTOR_ID = "contributor_id"
        const val EXTRA_TEMPLE_ID = "temple_id"
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
        setContentView(R.layout.activity_temple_request_history_detail_contributor)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mSharedPref = SharedPrefManager.getInstance(applicationContext)
        mSharedPrefLocation = SharedPrefLocationManager.getInstance(applicationContext)

        mViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            TempleDetailViewModel::class.java
        )

        button_temple_request_history_detail_contributor_call.setOnClickListener(this)
        button_temple_request_history_detail_contributor_route.setOnClickListener(this)

        val temple = intent.getParcelableExtra<Temple>(EXTRA_TEMPLE)
        if (temple != null) {
            showTempleDetail(temple)
        } else {
            val requestType = intent.extras?.getString(EXTRA_REQUEST_TYPE)
            val contributorId = intent.extras?.getString(EXTRA_CONTRIBUTOR_ID)
            val templeId = intent.extras?.getString(EXTRA_TEMPLE_ID)
            if (requestType != null && contributorId != null && templeId != null)
                mViewModel.getTempleRequestHistoryDetailContributor(
                    requestType,
                    contributorId,
                    templeId
                )
                    .observe(this, Observer {
                        if (it != null)
                            showTempleDetail(it)
                    })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_temple_request_history_detail_contributor_call -> {
                val callIntent =
                    Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mTemple.caretakerPhone))
                startActivity(callIntent)
            }
            R.id.button_temple_request_history_detail_contributor_route -> {
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
            text_temple_request_history_detail_contributor_distance.text = distanceDuration[0]
            text_temple_request_history_detail_contributor_duration.text = distanceDuration[1]

            when (temple.requestStatus) {
                "accepted" -> {
                    text_temple_request_history_detail_contributor_label.text =
                        getString(R.string.temple_request_detail_text_label_accepted)
                    text_temple_request_history_detail_contributor_label.background =
                        getDrawable(R.color.colorGreen)
                }
                "rejected" -> {
                    text_temple_request_history_detail_contributor_label.text =
                        getString(R.string.temple_request_detail_text_label_rejected)
                    text_temple_request_history_detail_contributor_label.background =
                        getDrawable(R.color.colorRed)
                }
                "deleted_by_admin" -> {
                    text_temple_request_history_detail_contributor_label.text =
                        getString(R.string.temple_request_detail_text_label_deleted_by_admin)
                    text_temple_request_history_detail_contributor_label.background =
                        getDrawable(R.color.colorOrange)
                }
                "edited_by_admin" -> {
                    text_temple_request_history_detail_contributor_label.text =
                        getString(R.string.temple_request_detail_text_label_edited_by_admin)
                    text_temple_request_history_detail_contributor_label.background =
                        getDrawable(R.color.colorOrange)
                }
            }

            carousel_temple_request_history_detail_contributor_photo.setImageListener { _, imageView ->
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(this).load(temple.photo).into(imageView)
            }
            carousel_temple_request_history_detail_contributor_photo.pageCount =
                mSamplePhoto.count()
            text_temple_request_history_detail_contributor_name.text = temple.name
            text_temple_request_history_detail_contributor_address.text = String.format(
                "%s, %s, %s",
                temple.address,
                temple.villageOffice,
                temple.subDistrict
            )
            text_temple_request_history_detail_contributor_caretaker_name.text =
                temple.caretakerName
            text_temple_request_history_detail_contributor_caretaker_phone.text =
                temple.caretakerPhone
            text_temple_request_history_detail_contributor_feast_day.text = temple.feastDay
            text_temple_request_history_detail_contributor_feast_day_start.text =
                temple.feastDayPrayerStart
            text_temple_request_history_detail_contributor_feast_day_end.text =
                temple.feastDayPrayerEnd
            text_temple_request_history_detail_contributor_full_moon_prayer_start.text =
                temple.fullMoonPrayerStart
            text_temple_request_history_detail_contributor_full_moon_prayer_end.text =
                temple.fullMoonPrayerEnd
            text_temple_request_history_detail_contributor_dead_moon_prayer_start.text =
                temple.deadMoonPrayerStart
            text_temple_request_history_detail_contributor_dead_moon_prayer_end.text =
                temple.deadMoonPrayerEnd
            if (temple.galunganPrayerStart != "") {
                linear_temple_request_history_detail_contributor_galungan.visibility = View.VISIBLE
                text_temple_request_history_detail_contributor_galungan_prayer_start.text =
                    temple.galunganPrayerStart
                text_temple_request_history_detail_contributor_galungan_prayer_end.text =
                    temple.galunganPrayerEnd
            }
            if (temple.kuninganPrayerStart != "") {
                linear_temple_request_history_detail_contributor_kuningan.visibility = View.VISIBLE
                text_temple_request_history_detail_contributor_kuningan_prayer_start.text =
                    temple.kuninganPrayerStart
                text_temple_request_history_detail_contributor_kuningan_prayer_end.text =
                    temple.kuninganPrayerEnd
            }
            if (temple.saraswatiPrayerStart != "") {
                linear_temple_request_history_detail_contributor_saraswati.visibility = View.VISIBLE
                text_temple_request_history_detail_contributor_saraswati_prayer_start.text =
                    temple.saraswatiPrayerStart
                text_temple_request_history_detail_contributor_saraswati_prayer_end.text =
                    temple.saraswatiPrayerEnd
            }
            if (temple.pagerwesiPrayerStart != "") {
                linear_temple_request_history_detail_contributor_pagerwesi.visibility = View.VISIBLE
                text_temple_request_history_detail_contributor_pagerwesi_prayer_start.text =
                    temple.pagerwesiPrayerStart
                text_temple_request_history_detail_contributor_pagerwesi_prayer_end.text =
                    temple.pagerwesiPrayerEnd
            }
            if (temple.pagerwesiPrayerStart != "") {
                linear_temple_request_history_detail_contributor_pagerwesi.visibility = View.VISIBLE
                text_temple_request_history_detail_contributor_pagerwesi_prayer_start.text =
                    temple.pagerwesiPrayerStart
                text_temple_request_history_detail_contributor_pagerwesi_prayer_end.text =
                    temple.pagerwesiPrayerEnd
            }
            if (temple.siwaratriPrayerStart != "") {
                linear_temple_request_history_detail_contributor_siwaratri.visibility = View.VISIBLE
                text_temple_request_history_detail_contributor_siwaratri_prayer_start.text =
                    temple.siwaratriPrayerStart
                text_temple_request_history_detail_contributor_siwaratri_prayer_end.text =
                    temple.siwaratriPrayerEnd
            }
            if (temple.melukatPrayerStart != "") {
                linear_temple_request_history_detail_contributor_melukat.visibility = View.VISIBLE
                text_temple_request_history_detail_contributor_melukat_information.visibility =
                    View.VISIBLE
                text_temple_request_history_detail_contributor_melukat_prayer_start.text =
                    temple.melukatPrayerStart
                text_temple_request_history_detail_contributor_melukat_prayer_end.text =
                    temple.melukatPrayerEnd
                text_temple_request_history_detail_contributor_melukat_information.text =
                    temple.melukatInformation
            }
            if (!temple.prayerEquipmentSellerChecked) {
                text_temple_request_history_detail_contributor_prayer_equipment_seller.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_close_grey_14dp,
                    0,
                    0,
                    0
                )
            }
            if (!temple.foodDrinkSellerChecked) {
                text_temple_request_history_detail_contributor_food_drink_seller.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_close_grey_14dp,
                    0,
                    0,
                    0
                )
            }

            if (temple.contributorNote != "")
                edit_temple_request_history_detail_contributor_note_for_admin.setText(temple.contributorNote)
            else
                edit_temple_request_history_detail_contributor_note_for_admin.setText("-")
            if (temple.adminNote != "")
                edit_temple_request_history_detail_contributor_admin_note.setText(temple.adminNote)
            else
                edit_temple_request_history_detail_contributor_admin_note.setText("-")
            view_temple_request_history_detail_contributor_background.visibility = View.GONE
            progress_temple_request_history_detail_contributor.visibility = View.GONE
        })
    }
}
