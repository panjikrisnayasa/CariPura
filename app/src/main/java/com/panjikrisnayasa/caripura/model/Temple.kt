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
    var lng: String = "",
    var caretakerName: String = "",
    var caretakerPhone: String = "",
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
    var prayerEquipmentSellerChecked: Boolean = false,
    var foodDrinkSellerChecked: Boolean = false,
    var adminNote: String = "",
    var contributorNote: String = "",
    var contributorId: String = "",
    var contributorFullName: String = "",
    var requestStatus: String = ""
) : Parcelable