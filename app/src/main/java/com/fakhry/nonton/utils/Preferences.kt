package com.fakhry.nonton.utils

import android.content.Context
import android.content.SharedPreferences

//DATABASE SEMENTARA AGAR KALAU SUDAH LOGIN, BERIKUTNYA TIDAK USAH LOGIN LAGI
class Preferences(val context: Context) {
    companion object {
        const val MEETING_PREF = "USER_PREF"
    }

    val sharedPref = context.getSharedPreferences(MEETING_PREF, 0)

    //INI ADALAH FUNGSI EDIT VALUE
    fun setValues(key: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    //INI ADALAH FUNGSI AMBIL VALUE
    fun getValues(key: String): String? {
        return sharedPref.getString(key, "")
    }
}