package com.panjikrisnayasa.caripura.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    var id: String = "",
    var fullName: String = "",
    var phoneNumber: String = "",
    var email: String = "",
    var role: String = ""
) : Parcelable