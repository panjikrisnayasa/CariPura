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
import com.panjikrisnayasa.caripura.viewmodel.EditTempleViewModel
import kotlinx.android.synthetic.main.activity_add_edit_temple_second.*
import org.json.JSONException
import org.json.JSONObject

class EditTempleSecondActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_NEW_TEMPLE = "new_temple"
        const val RESULT_EDIT_2 = 302
    }

    private lateinit var mViewModel: EditTempleViewModel
    private lateinit var mSharedPref: SharedPrefManager
    private lateinit var mNewTemple: Temple
    private lateinit var mOldTemple: Temple

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_temple_second)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mSharedPref = SharedPrefManager.getInstance(this)

        mViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(EditTempleViewModel::class.java)

        title = getString(R.string.label_edit_temple)
        button_add_edit_temple_second_add_edit.text =
            getString(R.string.add_edit_temple_second_button_edit)

        if (mSharedPref.getRole() == "contributor") {
            title = getString(R.string.label_edit_temple_request)
            linear_add_edit_temple_second_note_for_admin.visibility = View.VISIBLE
        }

        val oldTemple = intent.getParcelableExtra<Temple>(EditTempleFirstActivity.EXTRA_OLD_TEMPLE)
        val newTemple = intent.getParcelableExtra<Temple>(EXTRA_NEW_TEMPLE)
        if (oldTemple != null && newTemple != null) {
            mNewTemple = Temple()
            mNewTemple = newTemple
            mOldTemple = Temple()
            mOldTemple = oldTemple
            showTempleDetail(oldTemple)
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
                mNewTemple.fullMoonPrayerStart =
                    text_add_edit_temple_second_full_moon_prayer_start.text.toString().trim()
                mNewTemple.fullMoonPrayerEnd =
                    text_add_edit_temple_second_full_moon_prayer_end.text.toString().trim()
                mNewTemple.deadMoonPrayerStart =
                    text_add_edit_temple_second_dead_moon_prayer_start.text.toString().trim()
                mNewTemple.deadMoonPrayerEnd =
                    text_add_edit_temple_second_dead_moon_prayer_end.text.toString().trim()
                mNewTemple.deadMoonPrayerStart =
                    text_add_edit_temple_second_dead_moon_prayer_start.text.toString().trim()
                if (check_add_edit_temple_second_galungan.isChecked) {
                    mNewTemple.galunganPrayerStart =
                        text_add_edit_temple_second_galungan_prayer_start.text.toString().trim()
                    mNewTemple.galunganPrayerEnd =
                        text_add_edit_temple_second_galungan_prayer_end.text.toString().trim()
                }
                if (check_add_edit_temple_second_kuningan.isChecked) {
                    mNewTemple.kuninganPrayerStart =
                        text_add_edit_temple_second_kuningan_prayer_start.text.toString().trim()
                    mNewTemple.kuninganPrayerEnd =
                        text_add_edit_temple_second_kuningan_prayer_end.text.toString().trim()
                }
                if (check_add_edit_temple_second_saraswati.isChecked) {
                    mNewTemple.saraswatiPrayerStart =
                        text_add_edit_temple_second_saraswati_prayer_start.text.toString().trim()
                    mNewTemple.saraswatiPrayerEnd =
                        text_add_edit_temple_second_saraswati_prayer_end.text.toString().trim()
                }
                if (check_add_edit_temple_second_pagerwesi.isChecked) {
                    mNewTemple.pagerwesiPrayerStart =
                        text_add_edit_temple_second_pagerwesi_prayer_start.text.toString().trim()
                    mNewTemple.pagerwesiPrayerEnd =
                        text_add_edit_temple_second_pagerwesi_prayer_end.text.toString().trim()
                }
                if (check_add_edit_temple_second_siwaratri.isChecked) {
                    mNewTemple.siwaratriPrayerStart =
                        text_add_edit_temple_second_siwaratri_prayer_start.text.toString().trim()
                    mNewTemple.siwaratriPrayerEnd =
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
                        mNewTemple.melukatInformation = melukatInformation
                    }
                    mNewTemple.melukatPrayerStart =
                        text_add_edit_temple_second_melukat_prayer_start.text.toString().trim()
                    mNewTemple.melukatPrayerEnd =
                        text_add_edit_temple_second_melukat_prayer_end.text.toString().trim()
                }
                mNewTemple.prayerEquipmentSellerChecked =
                    check_add_edit_temple_second_prayer_equipment_seller.isChecked
                mNewTemple.foodDrinkSellerChecked =
                    check_add_edit_temple_second_food_drink_seller.isChecked
                val resultIntent = Intent()
                hideKeyboard()
                if (mSharedPref.getRole() == "contributor") {
                    val noteForAdmin =
                        edit_add_edit_temple_second_note_for_admin.text.toString().trim()
                    mNewTemple.contributorNote = noteForAdmin
                    if (!isNull) {
                        view_add_edit_temple_second_background.visibility = View.VISIBLE
                        progress_add_edit_temple_second.visibility = View.VISIBLE
                        mViewModel.editTempleRequest(mOldTemple, mNewTemple)
                            .observe(this, Observer {
                                if (it != null) {
                                    val topic = "/topics/request"
                                    val notification = JSONObject()
                                    val notificationBody = JSONObject()
                                    try {
                                        notificationBody.put(
                                            "title", "Pengajuan Ubah Pura"
                                        )
                                        notificationBody.put(
                                            "message",
                                            "${mOldTemple.name} diajukan untuk diubah"
                                        )
                                        notificationBody.put("requestType", "edit_request")
                                        notificationBody.put(
                                            "contributorId",
                                            mNewTemple.contributorId
                                        )
                                        notificationBody.put("templeId", mNewTemple.id)
                                        notification.put("to", topic)
                                        notification.put("data", notificationBody)
                                    } catch (e: JSONException) {
                                        e.message
                                    }
                                    mViewModel.sendNotification(notification, applicationContext)
                                    Toast.makeText(
                                        this,
                                        getString(R.string.toast_message_edit_temple_requested),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    setResult(RESULT_EDIT_2, resultIntent)
                                    finish()
                                }
                            })
                    }
                } else {
                    view_add_edit_temple_second_background.visibility = View.VISIBLE
                    progress_add_edit_temple_second.visibility = View.VISIBLE
                    mViewModel.editTemple(mOldTemple, mNewTemple).observe(this, Observer {
                        if (it != null) {
                            if (mNewTemple.contributorId != "") {
                                val topic = "/topics/approval_" + mNewTemple.contributorId
                                val notification = JSONObject()
                                val notificationBody = JSONObject()
                                try {
                                    notificationBody.put(
                                        "title", "Ubah Pura oleh Admin"
                                    )
                                    notificationBody.put(
                                        "message",
                                        String.format(
                                            "${mNewTemple.name} milik Anda telah diubah oleh Admin"
                                        )
                                    )
                                    notificationBody.put("requestType", "history_edit_request")
                                    notificationBody.put("contributorId", mNewTemple.contributorId)
                                    notificationBody.put("templeId", it)
                                    notification.put("to", topic)
                                    notification.put("data", notificationBody)
                                } catch (e: JSONException) {
                                    e.message
                                }
                                mViewModel.sendNotification(notification, applicationContext)
                            }
                            Toast.makeText(
                                this,
                                getString(R.string.toast_message_temple_edited),
                                Toast.LENGTH_SHORT
                            ).show()
                            setResult(RESULT_EDIT_2, resultIntent)
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
                    linear_add_edit_temple_second_galungan.visibility = View.GONE
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
                    text_add_edit_temple_second_melukat_prayer_start
                )
            }
        }
    }

    private fun showTempleDetail(temple: Temple) {
        text_add_edit_temple_second_full_moon_prayer_start.text = temple.fullMoonPrayerStart
        text_add_edit_temple_second_full_moon_prayer_end.text = temple.fullMoonPrayerEnd
        text_add_edit_temple_second_dead_moon_prayer_start.text = temple.deadMoonPrayerStart
        text_add_edit_temple_second_dead_moon_prayer_end.text = temple.deadMoonPrayerEnd
        if (temple.galunganPrayerStart != "") {
            check_add_edit_temple_second_galungan.isChecked = true
            linear_add_edit_temple_second_galungan.visibility = View.VISIBLE
            text_add_edit_temple_second_galungan_prayer_start.text = temple.galunganPrayerStart
            text_add_edit_temple_second_galungan_prayer_end.text = temple.galunganPrayerEnd
        }
        if (temple.kuninganPrayerStart != "") {
            check_add_edit_temple_second_kuningan.isChecked = true
            linear_add_edit_temple_second_kuningan.visibility = View.VISIBLE
            text_add_edit_temple_second_kuningan_prayer_start.text = temple.kuninganPrayerStart
            text_add_edit_temple_second_kuningan_prayer_end.text = temple.kuninganPrayerEnd
        }
        if (temple.saraswatiPrayerStart != "") {
            check_add_edit_temple_second_saraswati.isChecked = true
            linear_add_edit_temple_second_saraswati.visibility = View.VISIBLE
            text_add_edit_temple_second_saraswati_prayer_start.text = temple.saraswatiPrayerStart
            text_add_edit_temple_second_saraswati_prayer_end.text = temple.saraswatiPrayerEnd
        }
        if (temple.pagerwesiPrayerStart != "") {
            check_add_edit_temple_second_pagerwesi.isChecked = true
            linear_add_edit_temple_second_pagerwesi.visibility = View.VISIBLE
            text_add_edit_temple_second_pagerwesi_prayer_start.text = temple.pagerwesiPrayerStart
            text_add_edit_temple_second_pagerwesi_prayer_end.text = temple.pagerwesiPrayerEnd
        }
        if (temple.siwaratriPrayerStart != "") {
            check_add_edit_temple_second_siwaratri.isChecked = true
            linear_add_edit_temple_second_siwaratri.visibility = View.VISIBLE
            text_add_edit_temple_second_siwaratri_prayer_start.text = temple.siwaratriPrayerStart
            text_add_edit_temple_second_siwaratri_prayer_end.text = temple.siwaratriPrayerEnd
        }
        if (temple.melukatPrayerStart != "") {
            check_add_edit_temple_second_melukat.isChecked = true
            linear_add_edit_temple_second_melukat.visibility = View.VISIBLE
            linear_add_edit_temple_second_melukat_information.visibility = View.VISIBLE
            text_add_edit_temple_second_melukat_prayer_start.text = temple.melukatPrayerStart
            text_add_edit_temple_second_melukat_prayer_end.text = temple.melukatPrayerEnd
            edit_add_edit_temple_second_melukat_information.setText(temple.melukatInformation)
        }
        if (temple.prayerEquipmentSellerChecked)
            check_add_edit_temple_second_prayer_equipment_seller.isChecked = true
        if (temple.foodDrinkSellerChecked)
            check_add_edit_temple_second_food_drink_seller.isChecked = true
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
