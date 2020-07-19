package com.panjikrisnayasa.caripura.util

import android.content.Context
import android.content.SharedPreferences
import com.panjikrisnayasa.caripura.model.User

class SharedPrefManager(private val mContext: Context?) {

    private val SHARED_PREF_NAME = "caripurasharedpref"
    private val KEY_IS_LOGGED_IN = "is_logged_in"
    private val KEY_ID = "id"
    private val KEY_EMAIL = "email"
    private val KEY_FULL_NAME = "full_name"
    private val KEY_PHONE_NUMBER = "phone_number"
    private val KEY_ROLE = "role"

    private val mSharedPref: SharedPreferences? =
        mContext?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private var mInstance: SharedPrefManager? = null

        @Synchronized
        fun getInstance(context: Context?): SharedPrefManager {
            if (mInstance == null) {
                mInstance =
                    SharedPrefManager(context)
            }
            return mInstance as SharedPrefManager
        }
    }

    fun setLogin(user: User) {
        val editor = mSharedPref?.edit()
        editor?.putBoolean(KEY_IS_LOGGED_IN, true)
        editor?.putString(KEY_ID, user.id)
        editor?.putString(KEY_EMAIL, user.email)
        editor?.putString(KEY_FULL_NAME, user.fullName)
        editor?.putString(KEY_PHONE_NUMBER, user.phoneNumber)
        editor?.putString(KEY_ROLE, user.role)
        editor?.apply()
    }

    fun isLoggedIn(): Boolean {
        return mSharedPref?.getBoolean(KEY_IS_LOGGED_IN, false) as Boolean
    }

    fun getId(): String {
        return mSharedPref?.getString(KEY_ID, "") as String
    }

    fun getEmail(): String {
        return mSharedPref?.getString(KEY_EMAIL, "") as String
    }

    fun getFullName(): String {
        return mSharedPref?.getString(KEY_FULL_NAME, "") as String
    }

    fun getPhoneNumber(): String {
        return mSharedPref?.getString(KEY_PHONE_NUMBER, "") as String
    }

    fun getRole(): String {
        return mSharedPref?.getString(KEY_ROLE, "") as String
    }

    fun logout() {
        val sharedPreferences =
            mContext?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putBoolean(KEY_IS_LOGGED_IN, false)
        editor?.clear()?.apply()
    }
}