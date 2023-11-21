package org.helios.mythicdoors.utils

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import org.helios.mythicdoors.store.StoreManager
import org.helios.mythicdoors.utils.interfaces.IPermissionTools

class LocationTools(
    private var timeInterval: Long,
    private var minimalDistance: Float
) {

    private val store: StoreManager by lazy { StoreManager.getInstance() }
    private val context: Context? by lazy { store.getContext() }
    private val permissionsTool: IPermissionTools by lazy { PermissionToolsImp() }

    private var locationClient: FusedLocationProviderClient? = try {
        context?.let { LocationServices.getFusedLocationProviderClient(it) } ?: throw Exception("Context is null")
    } catch (e: Exception) {
        Log.e("LocationTools", "Error getting FusedLocationProviderClient: ${e.message}")
        null
    }

//    private val locationCallback = object: LocationCallback() {
//        fun onLocationResult(locationResult: LocationResult?) {
//            locationResult ?: throw Exception("LocationResult is null")
//
//            super.onLocationResult(locationResult)
//            locationResult.lastLocation.let {
//                /* TODO: Save in the database */
//            }
//        }
//    }

    private val locationRequest: LocationRequest = LocationRequest.Builder(timeInterval).apply {
        setMinUpdateDistanceMeters(minimalDistance)
        setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        setWaitForAccurateLocation(true)
    }.build()

//    fun startLocationUpdates() {
//        if (context == null) throw Exception("Context is null")
//
//        val permissions = arrayOf(
//            android.Manifest.permission.ACCESS_FINE_LOCATION,
//            android.Manifest.permission.ACCESS_COARSE_LOCATION
//        )
//
//        if (!permissionsTool.arePermissionsGranted(context!!, permissions)) { permissionsTool.requestPermissions(context!!, permissions, 1) }
//
//        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
//            if (isGranted) {
//                locationClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
//            } else {
//
//
//            }            }
//
//
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            permissionsTool.requestPermissions(context!!, permissions, 1)
//
//
//
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        locationClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
//
//    }


}