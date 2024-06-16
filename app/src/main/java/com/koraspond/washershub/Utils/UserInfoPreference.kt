package com.koraspond.washershub.Utils

import android.content.Context

class UserInfoPreference(private val context: Context) {
    fun setInt(key: String?, value: Int) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = sharedPref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String?): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getInt(key, 0)
    }

    fun setStr(key: String?, value: String?) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStr(key: String?): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getString(key, "DNF")
    }

    fun setBool(key: String?, value: Boolean) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBool(key: String?): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getBoolean(key, false)
    }

    fun removeFromPrefs(key: String?) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = sharedPref.edit()
        editor.remove(key)
        editor.apply()
    }

    fun removeAllPrefData() {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = sharedPref.edit()
        editor.clear().commit()
    }

    companion object {
        const val PREFS_NAME = "USER_PREFERENCE"
    }
}