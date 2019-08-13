package com.unitbean.studentappkotlin.utils.repository

import android.content.Context
import android.content.SharedPreferences
import com.unitbean.studentappkotlin.R

class Preferences (private val context: Context) {

    val prefs: SharedPreferences
        get() = context.getSharedPreferences(
            context.getString(
                R.string.key_settings),
            Context.MODE_PRIVATE
        )

    val prefsEditor: SharedPreferences.Editor
        get() = prefs.edit()
}