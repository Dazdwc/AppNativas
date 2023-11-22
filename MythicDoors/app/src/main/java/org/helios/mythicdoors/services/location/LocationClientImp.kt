package org.helios.mythicdoors.services.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import org.helios.mythicdoors.utils.hasLocationPermission

class LocationClientImp(
    private val context: Context,
    private val client: FusedLocationProviderClient
): ILocationClient {
    /* We can suppress the Missing Permission Check because we have externalized the logic in a util */
    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {
            if (!context.hasLocationPermission()) throw ILocationClient.LocationException("Location permission not granted")

            val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val isConnectionException: Boolean = !checkIfServicesAreEnabled(locationManager)
            isConnectionException
                .takeIf { it }
                ?.let { throw ILocationClient.LocationException("Location services not enabled") }

            val request = createRequest(interval)

            val locationCallback = object: LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)

                    result.locations.lastOrNull()?.let{ location ->
                        launch { send(location)  }
                    }
                }
            }

            client.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }

    private fun checkIfServicesAreEnabled(locationManager: LocationManager): Boolean {
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        return isGpsEnabled && isNetworkEnabled
    }

    private fun createRequest(interval: Long): LocationRequest {
        return LocationRequest.Builder(interval).apply {
            setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            setWaitForAccurateLocation(true)
        }.build()
    }
}