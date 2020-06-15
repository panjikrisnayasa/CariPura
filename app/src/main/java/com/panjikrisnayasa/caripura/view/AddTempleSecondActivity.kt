package com.panjikrisnayasa.caripura.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.model.Temple
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import com.panjikrisnayasa.caripura.viewmodel.AddTempleViewModel
import kotlinx.android.synthetic.main.activity_add_edit_temple_second.*
import org.json.JSONException
import org.json.JSONObject

class AddTempleSecondActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_TEMPLE = "temple"
        const val RESULT_ADD_2 = 102
    }

    private lateinit var mSharedPref: SharedPrefManager
    private lateinit var mViewModel: AddTempleViewModel
    private lateinit var mTemple: Temple

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_temple_second)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mSharedPref = SharedPrefManager.getInstance(this)

        mViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(AddTempleViewModel::class.java)

        if (mSharedPref.getRole() == "contributor") {
            title = getString(R.string.label_add_temple_request)
            linear_add_edit_temple_second_note_for_admin.visibility = View.VISIBLE
        }

        val temple = intent.getParcelableExtra<Temple>(EXTRA_TEMPLE)
        if (temple != null) {
            mTemple = temple
        }

        button_add_edit_temple_second_add_edit.setOnClickListener(this)
        text_add_edit_temple_second_full_moon_prayer_start.setOnClickListener(this)
        text_add_edit_temple_second_full_moon_prayer_end.setOnClickListener(this)
        text_add_edit_temple_second_dead_moon_prayer_start.setOnClickListener(this)
        text_add_edit_temple_second_dead_moon_prayer_end.setOnClickListener(this)
        check_add_edit_temple_second_galungan.setOnClickListener(this)
        text_add_edit_temple_second_galungan_prayer_start.setOnClickListener(this)
        text_add_edit_temple_second_galungan_prayer_end.setOnClickListener(this)
        check_add_edit_temple_second_kuningan.setOnClickListener(this)
        text_add_edit_temple_second_kuningan_prayer_start.setOnClickListener(this)
        text_add_edit_temple_second_kuningan_prayer_end.setOnClickListener(this)
        check_add_edit_temple_second_saraswati.setOnClickListener(this)
        text_add_edit_temple_second_saraswati_prayer_start.setOnClickListener(this)
        text_add_edit_temple_second_saraswati_prayer_end.setOnClickListener(this)
        check_add_edit_temple_second_pagerwesi.setOnClickListener(this)
        text_add_edit_temple_second_pagerwesi_prayer_start.setOnClickListener(this)
        text_add_edit_temple_second_pagerwesi_prayer_end.setOnClickListener(this)
        check_add_edit_temple_second_siwaratri.setOnClickListener(this)
        text_add_edit_temple_second_siwaratri_prayer_start.setOnClickListener(this)
        text_add_edit_temple_second_siwaratri_prayer_end.setOnClickListener(this)
        check_add_edit_temple_second_melukat.setOnClickListener(this)
        text_add_edit_temple_second_melukat_prayer_start.setOnClickListener(this)
        text_add_edit_temple_second_melukat_prayer_end.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_add_edit_temple_second_add_edit -> {
                var isNull = false
                mTemple.fullMoonPrayerStart =
                    text_add_edit_temple_second_full_moon_prayer_start.text.toString().trim()
                mTemple.fullMoonPrayerEnd =
                    text_add_edit_temple_second_full_moon_prayer_end.text.toString().trim()
                mTemple.deadMoonPrayerStart =
                    text_add_edit_temple_second_dead_moon_prayer_start.text.toString().trim()
                mTemple.deadMoonPrayerEnd =
                    text_add_edit_temple_second_dead_moon_prayer_end.text.toString().trim()
                mTemple.deadMoonPrayerStart =
                    text_add_edit_temple_second_dead_moon_prayer_start.text.toString().trim()
                if (check_add_edit_temple_second_galungan.isChecked) {
                    mTemple.galunganPrayerStart =
                        text_add_edit_temple_second_galungan_prayer_start.text.toString().trim()
                    mTemple.galunganPrayerEnd =
                        text_add_edit_temple_second_galungan_prayer_end.text.toString().trim()
                }
                if (check_add_edit_temple_second_kuningan.isChecked) {
                    mTemple.kuninganPrayerStart =
                        text_add_edit_temple_second_kuningan_prayer_start.text.toString().trim()
                    mTemple.kuninganPrayerEnd =
                        text_add_edit_temple_second_kuningan_prayer_end.text.toString().trim()
                }
                if (check_add_edit_temple_second_saraswati.isChecked) {
                    mTemple.saraswatiPrayerStart =
                        text_add_edit_temple_second_saraswati_prayer_start.text.toString().trim()
                    mTemple.saraswatiPrayerEnd =
                        text_add_edit_temple_second_saraswati_prayer_end.text.toString().trim()
                }
                if (check_add_edit_temple_second_pagerwesi.isChecked) {
                    mTemple.pagerwesiPrayerStart =
                        text_add_edit_temple_second_pagerwesi_prayer_start.text.toString().trim()
                    mTemple.pagerwesiPrayerEnd =
                        text_add_edit_temple_second_pagerwesi_prayer_end.text.toString().trim()
                }
                if (check_add_edit_temple_second_siwaratri.isChecked) {
                    mTemple.siwaratriPrayerStart =
                        text_add_edit_temple_second_siwaratri_prayer_start.text.toString().trim()
                    mTemple.siwaratriPrayerEnd =
                        text_add_edit_temple_second_siwaratri_prayer_end.text.toString().trim()
                }
                if (check_add_edit_temple_second_melukat.isChecked) {
                    val melukatInformation =
                        edit_add_edit_temple_second_melukat_information.text.toString().trim()
                    if (melukatInformation.isBlank()) {
                        isNull = true
                        edit_add_edit_temple_second_melukat_information.error =
                            getString(R.string.error_message_fill_this_field)
                    } else {
                        mTemple.melukatInformation = melukatInformation
                    }
                    mTemple.melukatPrayerStart =
                        text_add_edit_temple_second_melukat_prayer_start.text.toString().trim()
                    mTemple.melukatPrayerEnd =
                        text_add_edit_temple_second_melukat_prayer_end.text.toString().trim()
                }
                mTemple.prayerEquipmentSellerChecked =
                    check_add_edit_temple_second_prayer_equipment_seller.isChecked
                mTemple.foodDrinkSellerChecked =
                    check_add_edit_temple_second_food_drink_seller.isChecked
                val resultIntent = Intent()
                hideKeyboard()
                if (mSharedPref.getRole() == "contributor") {
                    val noteForAdmin =
                        edit_add_edit_temple_second_note_for_admin.text.toString().trim()
                    mTemple.contributorNote = noteForAdmin
                    mTemple.contributorId = mSharedPref.getId()
                    mTemple.contributorFullName = mSharedPref.getFullName()

                    if (!isNull) {
                        view_add_edit_temple_second_background.visibility = View.VISIBLE
                        progress_add_edit_temple_second.visibility = View.VISIBLE
                        mViewModel.addTempleRequest(mTemple).observe(this, Observer {
                            if (it != null) {
                                val topic = "/topics/request"
                                val notification = JSONObject()
                                val notificationBody = JSONObject()
                                try {
                                    notificationBody.put(
                                        "title", "Pengajuan Tambah Pura"
                                    )
                                    notificationBody.put(
                                        "message",
                                        String.format(
                                            "${mTemple.name} diajukan untuk ditambahkan"
                                        )
                                    )
                                    notificationBody.put("requestType", "add_request")
                                    notificationBody.put("contributorId", mTemple.contributorId)
                                    notificationBody.put("templeId", mTemple.id)
                                    notification.put("to", topic)
                                    notification.put("data", notificationBody)
                                } catch (e: JSONException) {
                                    e.message
                                }
                                mViewModel.sendNotification(notification, applicationContext)
                                Toast.makeText(
                                    this,
                                    getString(R.string.toast_message_add_temple_requested),
                                    Toast.LENGTH_SHORT
                                ).show()
                                setResult(RESULT_ADD_2, resultIntent)
                                finish()
                            }
                        })
                    }
                } else {
                    view_add_edit_temple_second_background.visibility = View.VISIBLE
                    progress_add_edit_temple_second.visibility = View.VISIBLE
                    mViewModel.addTemple(mTemple).observe(this, Observer {
                        if (it != null) {
                            Toast.makeText(
                                this,
                                getString(R.string.toast_message_temple_added),
                                Toast.LENGTH_SHORT
                            ).show()
                            setResult(RESULT_ADD_2, resultIntent)
                            finish()
                        }
                    })
                }
            }
            R.id.text_add_edit_temple_second_full_moon_prayer_start -> {
                AddTempleFirstActivity.showTimePicker(
                    this,
                    text_add_edit_temple_second_full_moon_prayer_start
                )
            }
            R.id.text_add_edit_temple_second_full_moon_prayer_end -> {
                AddTempleFirstActivity.showTimePicker(
                    this,
                    text_add_edit_temple_second_full_moon_prayer_end
                )
            }
            R.id.text_add_edit_temple_second_dead_moon_prayer_start -> {
                AddTempleFirstActivity.showTimePicker(
                    this,
                    text_add_edit_temple_second_dead_moon_prayer_start
                )
            }
            R.id.text_add_edit_temple_second_dead_moon_prayer_end -> {
                AddTempleFirstActivity.showTimePicker(
                    this,
                    text_add_edit_temple_second_dead_moon_prayer_end
                )
            }
            R.id.check_add_edit_temple_second_galungan -> {
                if (check_add_edit_temple_second_galungan.isChecked) {
                    linear_add_edit_temple_second_galungan.visibility = View.VISIBLE
                } else {
                    linear_add_edit_temple_second_galungan.visibility = View.GONE
                }
            }
            R.id.text_add_edit_temple_second_galungan_prayer_start -> {
                AddTempleFirstActivity.showTimePicker(
                    this,
                    text_add_edit_temple_second_galungan_prayer_start
                )
            }
            R.id.text_add_edit_temple_second_galungan_prayer_end -> {
                AddTempleFirstActivity.showTimePicker(
                    this,
                    text_add_edit_temple_second_galungan_prayer_end
                )
            }
            R.id.check_add_edit_temple_second_kuningan -> {
                if (check_add_edit_temple_second_kuningan.isChecked) {
                    linear_add_edit_temple_second_kuningan.visibility = View.VISIBLE
                } else {
                    linear_add_edit_temple_second_kuningan.visibility = View.GONE
                }
            }
            R.id.text_add_edit_temple_second_kuningan_prayer_start -> {
                AddTempleFirstActivity.showTimePicker(
                    this,
                    text_add_edit_temple_second_kuningan_prayer_start
                )
            }
            R.id.text_add_edit_temple_second_kuningan_prayer_end -> {
                AddTempleFirstActivity.showTimePicker(
                    this,
                    text_add_edit_temple_second_kuningan_prayer_end
                )
            }
            R.id.check_add_edit_temple_second_saraswati -> {
                if (check_add_edit_temple_second_saraswati.isChecked) {
                    linear_add_edit_temple_second_saraswati.visibility = View.VISIBLE
                } else {
                    linear_add_edit_temple_second_saraswati.visibility = View.GONE
                }
            }
            R.id.text_add_edit_temple_second_saraswati_prayer_start -> {
                AddTempleFirstActivity.showTimePicker(
                    this,
                    text_add_edit_temple_second_saraswati_prayer_start
                )
            }
            R.id.text_add_edit_temple_second_saraswati_prayer_end -> {
                AddTempleFirstActivity.showTimePicker(
                    this,
                    text_add_edit_temple_second_saraswati_prayer_end
                )
            }
            R.id.check_add_edit_temple_second_pagerwesi -> {
                if (check_add_edit_temple_second_pagerwesi.isChecked) {
                    linear_add_edit_temple_second_pagerwesi.visibility = View.VISIBLE
                } else {
                    linear_add_edit_temple_second_pagerwesi.visibility = View.GONE
                }
            }
            R.id.text_add_edit_temple_second_pagerwesi_prayer_start -> {
                AddTempleFirstActivity.showTimePicker(
                    this,
                    text_add_edit_temple_second_pagerwesi_prayer_start
                )
            }
            R.id.text_add_edit_temple_second_pagerwesi_prayer_end -> {
                AddTempleFirstActivity.showTimePicker(
                    this,
                    text_add_edit_temple_second_pagerwesi_prayer_end
                )
            }
            R.id.check_add_edit_temple_second_siwaratri -> {
                if (check_add_edit_temple_second_siwaratri.isChecked) {
                    linear_add_edit_temple_second_siwaratri.visibility = View.VISIBLE
                } else {
                    linear_add_edit_temple_second_siwaratri.visibility = View.GONE
                }
            }
            R.id.text_add_edit_temple_second_siwaratri_prayer_start -> {
                AddTempleFirstActivity.showTimePicker(
                    this,
                    text_add_edit_temple_second_siwaratri_prayer_start
                )
            }
            R.id.text_add_edit_temple_second_siwaratri_prayer_end -> {
                AddTempleFirstActivity.showTimePicker(
                    this,
                    text_add_edit_temple_second_siwaratri_prayer_end
                )
            }
            R.id.check_add_edit_temple_second_melukat -> {
                if (check_add_edit_temple_second_melukat.isChecked) {
                    linear_add_edit_temple_second_melukat.visibility = View.VISIBLE
                    linear_add_edit_temple_second_melukat_information.visibility = View.VISIBLE
                } else {
                    linear_add_edit_temple_second_melukat.visibility = View.GONE
                    linear_add_edit_temple_second_melukat_information.visibility = View.GONE
                }
            }
            R.id.text_add_edit_temple_second_melukat_prayer_start -> {
                AddTempleFirstActivity.showTimePicker(
                    this,
                    text_add_edit_temple_second_melukat_prayer_start
                )
            }
            R.id.text_add_edit_temple_second_melukat_prayer_end -> {
                AddTempleFirstActivity.showTimePicker(
                    this,
                    text_add_edit_temple_second_melukat_prayer_end
                )
            }
        }
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
