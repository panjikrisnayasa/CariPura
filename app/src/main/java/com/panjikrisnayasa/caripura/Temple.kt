package com.panjikrisnayasa.caripura

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Temple(
    var id: String = "",
    var photo: String = "",
    var name: String = "",
    var villageOffice: String = "",
    var lat: String = "",
    var long: String = "",
    var distance: String = "",
    var fullMoonPrayerStart: String = "",
    var fullMoonPrayerEnd: String = "",
    var deadMoonPrayerStart: String = "",
    var deadMoonPrayerEnd: String = ""
) : Parcelable