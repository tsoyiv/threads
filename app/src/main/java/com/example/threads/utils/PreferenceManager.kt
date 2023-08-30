package com.example.threads.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "MyPrefs"
        private const val AUTH_TOKEN_KEY = "authToken"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var authToken: String?
        get() = prefs.getString(AUTH_TOKEN_KEY, null)
        set(value) {
            prefs.edit().putString(AUTH_TOKEN_KEY, value).apply()
        }
}
