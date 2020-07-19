package com.panjikrisnayasa.caripura.util

import android.content.Context
import android.content.SharedPreferences

class SharedPrefLocationManager(mContext: Context?) {

    private val SHARED_PREF_NAME = "caripuralocationsharedpref"
    private val KEY_LAST_LAT = "last_lat"
    private val KEY_LAST_LNG = "last_lng"

    private val mSharedPref: SharedPreferences? =
        mContext?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private var mInstance: SharedPrefLocationManager? = null

        @Synchronized
        fun getInstance(context: Context?): SharedPrefLocationManager {
            if (mInstance == null) {
                mInstance =
                    SharedPrefLocationManager(context)
            }
            return mInstance as SharedPrefLocationManager
        }
    }

    fun setLastLocation(lastLat: String, lastLng: String) {
        val editor = mSharedPref?.edit()
        editor?.putString(KEY_LAST_LAT, lastLat)
        editor?.putString(KEY_LAST_LNG, lastLng)
        editor?.apply()
    }

    fun getLastLat(): String {
        return mSharedPref?.getString(KEY_LAST_LAT, "") as String
    }

    fun getLastLng(): String {
        return mSharedPref?.getString(KEY_LAST_LNG, "") as String
    }
}