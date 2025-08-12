package com.eselman.cities.challenge.utils

import android.content.Context
import android.content.pm.PackageManager

fun Context.getMapsApiKey(): String {
    packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).apply {
        val mapsApiKey = metaData?.getString("com.google.android.geo.API_KEY")
        return mapsApiKey ?: ""
    }
}
