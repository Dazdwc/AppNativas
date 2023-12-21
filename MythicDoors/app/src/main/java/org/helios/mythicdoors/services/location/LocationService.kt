package org.helios.mythicdoors.services.location

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.model.entities.Location
import org.helios.mythicdoors.store.StoreManager
import org.helios.mythicdoors.utils.AppConstants
import org.helios.mythicdoors.utils.AppConstants.NotificationChannels.LOCATION_NOTIFICATION_CHANNEL
import org.helios.mythicdoors.utils.notifications.NotificationFabric

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class LocationService(): Service() {

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_LOCATION_UPDATE = "ACTION_LOCATION_UPDATE"

        lateinit var instance: LocationService
            private set
    }

    private val serviceScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: ILocationClient

    private val store: StoreManager by lazy { StoreManager.getInstance() }

    private lateinit var locationCoordinates: Map<String, Double>

    private var _locationLiveData = MutableLiveData<Map<String, Double>>()
    val locationLiveData get() = _locationLiveData

    var locationCallback: ILocationCallback? = null

    override fun onBind(intent: Intent?): IBinder? { return null }

    override fun onCreate() {
        super.onCreate()
        locationClient = LocationClientImp(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        instance = this
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
        serviceScope.cancel()
    }

    private fun start() {
        val notification = NotificationFabric.createNotificationBuilder(LOCATION_NOTIFICATION_CHANNEL)

        locationClient.getLocationUpdates(1000L)
            .catch { e ->
                Log.e("LocationService", "Error getting location updates: ${e.printStackTrace()}")
            }
            .onEach { location ->
                this.locationCoordinates = mapOf(
                    "latitude" to location.latitude,
                    "longitude" to location.longitude
                )
                _locationLiveData.postValue(locationCoordinates)
                locationCallback?.onLocationUpdate(locationCoordinates)

                updateNotification(notification)
            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())
    }

    private fun stop() {
        stopForeground(STOP_FOREGROUND_DETACH)
        stopSelf()
    }

    fun updateLocation() {
        val dataController = MainActivity.dataController

        serviceScope.launch {
            Location.create(
                store.getAppStore().actualUser?.getEmail() ?: return@launch,
                locationCoordinates["latitude"] ?: 0.0,
                locationCoordinates["longitude"] ?: 0.0
            ).also {
                dataController.saveLocation(it)
            }

            // TODO: Este bloque es simplemente para que el profe lo vea en el LOG
            val location = dataController.getLastLocation()
            Log.d("LocationService", "updateLocation: $location")
        }
    }

    private fun updateNotification(notification: NotificationCompat.Builder) {
        val updatedNotification = notification.setContentText("Your location has been set to: ${locationCoordinates["latitude"]}, ${locationCoordinates["longitude"]}")

        val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(AppConstants.NotificationIds.LOCATION_NOTIFICATION_ID, updatedNotification.build())
    }
}

interface ILocationCallback {
    fun onLocationUpdate(location: Map<String, Double>)
}