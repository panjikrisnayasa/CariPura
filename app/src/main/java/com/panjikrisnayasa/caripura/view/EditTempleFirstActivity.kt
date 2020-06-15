package com.panjikrisnayasa.caripura.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.model.Temple
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import kotlinx.android.synthetic.main.activity_add_edit_temple_first.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EditTempleFirstActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {

    companion object {
        const val EXTRA_OLD_TEMPLE = "old_temple"
        const val REQUEST_EDIT = 3
        const val RESULT_EDIT_1 = 301
    }

    private lateinit var mSharedPref: SharedPrefManager
    private lateinit var mNewTemple: Temple
    private lateinit var mOldTemple: Temple
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mCurrentPhotoPath: String
    private var mContentUri: Uri? = null
    private var mTempleLat = ""
    private var mTempleLng = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_temple_first)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        button_add_edit_temple_first_add_photo.text =
            getString(R.string.add_edit_temple_first_button_edit_photo)

        mSharedPref = SharedPrefManager.getInstance(this)

        if (mSharedPref.getRole() == "contributor") {
            title = getString(R.string.label_edit_temple_request)
        }

        val oldTemple = intent.getParcelableExtra<Temple>(EXTRA_OLD_TEMPLE)
        if (oldTemple != null) {
            mOldTemple = oldTemple
            mTempleLat = mOldTemple.lat
            mTempleLng = mOldTemple.lng
            mContentUri = mOldTemple.photo.toUri()
            mNewTemple = Temple()
            showTempleDetail(oldTemple)
        }

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_add_edit_temple_first_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        button_add_edit_temple_first_pick_location.setOnClickListener(this)
        button_add_edit_temple_first_add_photo.setOnClickListener(this)
        text_add_edit_temple_first_feast_day_prayer_start.setOnClickListener(this)
        text_add_edit_temple_first_feast_day_prayer_end.setOnClickListener(this)
        button_add_edit_temple_first_next.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(p0: GoogleMap?) {
        if (p0 != null)
            mGoogleMap = p0
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mGoogleMap.isMyLocationEnabled = false
        mGoogleMap.uiSettings.isMyLocationButtonEnabled = false
        mGoogleMap.uiSettings.setAllGesturesEnabled(false)

        val templeLatLng = LatLng(mTempleLat.toDouble(), mTempleLng.toDouble())
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(templeLatLng, 18f))
        mGoogleMap.addMarker(MarkerOptions().position(templeLatLng))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AddTempleFirstActivity.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            val file = File(mCurrentPhotoPath)
            mContentUri = Uri.fromFile(file)
            image_add_edit_temple_first_photo.setImageURI(mContentUri)
            image_add_edit_temple_first_photo.visibility = View.VISIBLE
            button_add_edit_temple_first_add_photo.text =
                getString(R.string.add_edit_temple_first_button_edit_photo)
        }
        if (requestCode == AddTempleFirstActivity.REQUEST_CHOOSE_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                mContentUri = data.data
                image_add_edit_temple_first_photo.setImageURI(mContentUri)
                image_add_edit_temple_first_photo.visibility = View.VISIBLE
                button_add_edit_temple_first_add_photo.text =
                    getString(R.string.add_edit_temple_first_button_edit_photo)
            }
        }
        if (data != null) {
            if (requestCode == REQUEST_EDIT) {
                if (resultCode == EditTempleSecondActivity.RESULT_EDIT_2) {
                    if (mSharedPref.getRole() == "contributor") {
                        val resultIntent = Intent()
                        setResult(RESULT_EDIT_1, resultIntent)
                    }
                    finish()
                }
            }
            if (requestCode == PickLocationActivity.REQUEST_PICK_LOCATION) {
                if (resultCode == PickLocationActivity.RESULT_PICK_LOCATION) {
                    val templeLat = data.getStringExtra("temple_latitude")
                    val templeLng = data.getStringExtra("temple_longitude")
                    if (templeLat != null && templeLng != null) {
                        mTempleLat = templeLat
                        mTempleLng = templeLng
                        val templeLatLng = LatLng(mTempleLat.toDouble(), mTempleLng.toDouble())
                        mGoogleMap.clear()
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(templeLatLng, 18f))
                        mGoogleMap.addMarker(MarkerOptions().position(templeLatLng))
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == AddTempleFirstActivity.CAMERA_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePhotoIntent()
            }
        } else if (requestCode == AddTempleFirstActivity.GALLERY_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchGalleryIntent()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_add_edit_temple_first_pick_location -> {
                val pickLocationIntent = Intent(this, PickLocationActivity::class.java)
                pickLocationIntent.putExtra("temple_lat", mTempleLat)
                pickLocationIntent.putExtra("temple_lng", mTempleLng)
                startActivityForResult(
                    pickLocationIntent,
                    PickLocationActivity.REQUEST_PICK_LOCATION
                )
            }
            R.id.button_add_edit_temple_first_add_photo -> {
                val builder = AlertDialog.Builder(this)
                builder.setItems(
                    AddTempleFirstActivity.OPTIONS
                ) { dialog, which ->
                    when {
                        AddTempleFirstActivity.OPTIONS[which] == "Take Photo" -> {
                            if (ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.CAMERA
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                ActivityCompat.requestPermissions(
                                    this,
                                    arrayOf(Manifest.permission.CAMERA),
                                    AddTempleFirstActivity.CAMERA_CODE
                                )
                            } else {
                                dispatchTakePhotoIntent()
                            }
                        }
                        AddTempleFirstActivity.OPTIONS[which] == "Choose from Gallery" -> {
                            if (ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                ActivityCompat.requestPermissions(
                                    this,
                                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                    AddTempleFirstActivity.GALLERY_CODE
                                )
                            } else {
                                dispatchGalleryIntent()
                            }
                        }
                        else -> {
                            dialog.dismiss()
                        }
                    }
                }
                builder.show()
            }
            R.id.text_add_edit_temple_first_feast_day_prayer_start -> {
                AddTempleFirstActivity.showTimePicker(
                    this,
                    text_add_edit_temple_first_feast_day_prayer_start
                )
            }
            R.id.text_add_edit_temple_first_feast_day_prayer_end -> {
                AddTempleFirstActivity.showTimePicker(
                    this,
                    text_add_edit_temple_first_feast_day_prayer_end
                )
            }
            R.id.button_add_edit_temple_first_next -> {
                var isNull = false

                mNewTemple.id = mOldTemple.id
                mNewTemple.adminNote = mOldTemple.adminNote
                mNewTemple.contributorNote = mOldTemple.contributorNote
                mNewTemple.contributorId = mOldTemple.contributorId
                mNewTemple.contributorFullName = mOldTemple.contributorFullName

                val name = edit_add_edit_temple_first_name.text.toString().trim()
                val address = edit_add_edit_temple_first_address.text.toString().trim()
                val villageOffice = edit_add_edit_temple_first_village_office.text.toString().trim()
                val subDistrict = edit_add_edit_temple_first_sub_district.text.toString().trim()
                val caretakerName = edit_add_edit_temple_first_caretaker_name.text.toString().trim()
                val caretakerPhone = edit_add_edit_temple_first_caretaker_phone.text.toString().trim()
                val feastDay = edit_add_edit_temple_first_feast_day.text.toString().trim()
                val feastDayPrayerStart =
                    text_add_edit_temple_first_feast_day_prayer_start.text.toString().trim()
                val feastDayPrayerEnd =
                    text_add_edit_temple_first_feast_day_prayer_end.text.toString().trim()

                if (!name.isNotBlank()) {
                    isNull = true
                    edit_add_edit_temple_first_name.error =
                        getString(R.string.error_message_fill_this_field)
                }
                if (!address.isNotBlank()) {
                    isNull = true
                    edit_add_edit_temple_first_address.error =
                        getString(R.string.error_message_fill_this_field)
                }
                if (!villageOffice.isNotBlank()) {
                    isNull = true
                    edit_add_edit_temple_first_village_office.error =
                        getString(R.string.error_message_fill_this_field)
                }
                if (!subDistrict.isNotBlank()) {
                    isNull = true
                    edit_add_edit_temple_first_sub_district.error =
                        getString(R.string.error_message_fill_this_field)
                }
                if (!caretakerName.isNotBlank()) {
                    isNull = true
                    edit_add_edit_temple_first_caretaker_name.error =
                        getString(R.string.error_message_fill_this_field)
                }
                if (!caretakerPhone.isNotBlank()) {
                    isNull = true
                    edit_add_edit_temple_first_caretaker_phone.error =
                        getString(R.string.error_message_fill_this_field)
                }
                if (!feastDay.isNotBlank()) {
                    isNull = true
                    edit_add_edit_temple_first_feast_day.error =
                        getString(R.string.error_message_fill_this_field)
                }
                if (!isNull) {
                    mNewTemple.name = name
                    mNewTemple.address = address
                    mNewTemple.villageOffice = villageOffice
                    mNewTemple.subDistrict = subDistrict
                    mNewTemple.lat = mTempleLat
                    mNewTemple.lng = mTempleLng
                    mNewTemple.caretakerName = caretakerName
                    mNewTemple.caretakerPhone = caretakerPhone
                    mNewTemple.feastDay = feastDay
                    mNewTemple.feastDayPrayerStart = feastDayPrayerStart
                    mNewTemple.feastDayPrayerEnd = feastDayPrayerEnd
                    mNewTemple.photo = mContentUri.toString()
                    val intent = Intent(this, EditTempleSecondActivity::class.java)
                    intent.putExtra(EXTRA_OLD_TEMPLE, mOldTemple)
                    intent.putExtra(EditTempleSecondActivity.EXTRA_NEW_TEMPLE, mNewTemple)
                    startActivityForResult(intent, REQUEST_EDIT)
                }
            }
        }
    }

    private fun showTempleDetail(temple: Temple) {
        edit_add_edit_temple_first_name.setText(temple.name)
        edit_add_edit_temple_first_address.setText(temple.address)
        edit_add_edit_temple_first_village_office.setText(temple.villageOffice)
        edit_add_edit_temple_first_sub_district.setText(temple.subDistrict)
        edit_add_edit_temple_first_caretaker_name.setText(temple.caretakerName)
        edit_add_edit_temple_first_caretaker_phone.setText(temple.caretakerPhone)
        edit_add_edit_temple_first_feast_day.setText(temple.feastDay)
        text_add_edit_temple_first_feast_day_prayer_start.text = temple.feastDayPrayerStart
        text_add_edit_temple_first_feast_day_prayer_end.text = temple.feastDayPrayerEnd
        Glide.with(this).load(temple.photo).into(image_add_edit_temple_first_photo)
        image_add_edit_temple_first_photo.visibility = View.VISIBLE
    }

    private fun dispatchTakePhotoIntent() {
        val takePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePhoto.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (photoFile != null) {
                val photoUri = FileProvider.getUriForFile(
                    this,
                    "com.panjikrisnayasa.caripura.fileprovider",
                    photoFile
                )
                takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(takePhoto, AddTempleFirstActivity.REQUEST_TAKE_PHOTO)
            }
        }
    }

    private fun createImageFile(): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val timeStamp = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val fileName = "JPEG_" + timeStamp + "_"
        val file = File.createTempFile(fileName, ".jpg", storageDirectory)
        mCurrentPhotoPath = file.absolutePath
        return file
    }

    private fun dispatchGalleryIntent() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(gallery, AddTempleFirstActivity.REQUEST_CHOOSE_FROM_GALLERY)
    }
}