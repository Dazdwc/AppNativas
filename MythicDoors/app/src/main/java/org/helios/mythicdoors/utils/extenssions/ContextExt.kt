package org.helios.mythicdoors.utils.extenssions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}

@RequiresApi(Build.VERSION_CODES.P)
fun Context.hasForegroundPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.FOREGROUND_SERVICE
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.hasCalendarPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_CALENDAR
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_CALENDAR
            ) == PackageManager.PERMISSION_GRANTED
}

// TODO: Check if this is the correct permission
fun Context.hasStoragePermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
}