package com.simtop.myweather.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

//Using this more than once, so moved to a separate class
abstract class PreferenceProvider(context : Context) {
    private val appContext = context.applicationContext

    protected val preferences : SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)
}