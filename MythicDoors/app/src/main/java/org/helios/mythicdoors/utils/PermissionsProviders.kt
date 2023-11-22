package org.helios.mythicdoors.utils

import android.Manifest
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.annotation.RequiresApi
import org.helios.mythicdoors.ui.fragments.*

@RequiresApi(TIRAMISU)
object AppPermissionsRequests {
    val appPermissionRequests: Array<String> = arrayOf(
        Manifest.permission.FOREGROUND_SERVICE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_MEDIA_IMAGES,
//        Manifest.permission.READ_EXTERNAL_STORAGE,
//        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//        Manifest.permission.READ_CALENDAR,
        Manifest.permission.WRITE_CALENDAR,
    )
}

@RequiresApi(TIRAMISU)
object PermissionsTextProviders {
    val permissionsTextProviders = mapOf(
        Manifest.permission.FOREGROUND_SERVICE to ForegroundServicePermissionTextProvider(),
        Manifest.permission.ACCESS_FINE_LOCATION to LocationPermissionTextProvider(),
        Manifest.permission.ACCESS_COARSE_LOCATION to LocationPermissionTextProvider(),
        Manifest.permission.READ_MEDIA_IMAGES to InternalImageStorePermissionTextProvider(),
        Manifest.permission.READ_EXTERNAL_STORAGE to InternalImageStorePermissionTextProvider(),
        Manifest.permission.WRITE_EXTERNAL_STORAGE to InternalImageStorePermissionTextProvider(),
        Manifest.permission_group.STORAGE to InternalImageStorePermissionTextProvider(),
        Manifest.permission.READ_CALENDAR to CalendarPermissionTextProvider(),
        Manifest.permission.WRITE_CALENDAR to CalendarPermissionTextProvider(),
    )
}