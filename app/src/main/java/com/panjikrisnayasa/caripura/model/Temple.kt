package com.panjikrisnayasa.caripura.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Temple(
    var id: String = "",
    var photo: String = "",
    var name: String = "",
    var address: String = "",
    var villageOffice: String = "",
    var subDistrict: String = "",
    var lat: String = "",
    var long: String = "",
    var distance: String = "",
    var caretakerName: String = "",
    var caretakerPhoneNumber: String = "",
    var feastDay: String = "",
    var feastDayPrayerStart: String = "",
    var feastDayPrayerEnd: String = "",
    var fullMoonPrayerStart: String = "",
    var fullMoonPrayerEnd: String = "",
    var deadMoonPrayerStart: String = "",
    var deadMoonPrayerEnd: String = "",
    var galunganPrayerStart: String = "",
    var galunganPrayerEnd: String = "",
    var kuninganPrayerStart: String = "",
    var kuninganPrayerEnd: String = "",
    var saraswatiPrayerStart: String = "",
    var saraswatiPrayerEnd: String = "",
    var pagerwesiPrayerStart: String = "",
    var pagerwesiPrayerEnd: String = "",
    var siwaratriPrayerStart: String = "",
    var siwaratriPrayerEnd: String = "",
    var melukatPrayerStart: String = "",
    var melukatPrayerEnd: String = "",
    var melukatInformation: String = "",
    var prayerNeedsSellerChecked: Boolean = false,
    var foodDrinkSellerChecked: Boolean = false
) : Parcelable