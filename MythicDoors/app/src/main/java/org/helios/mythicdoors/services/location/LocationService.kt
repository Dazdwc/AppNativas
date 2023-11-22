package org.helios.mythicdoors.services.location

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.helios.mythicdoors.utils.AppConstants.NotificationChannels.LOCATION_NOTIFICATION_CHANNEL

class LocationService(): Service() {

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

    private val serviceScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: ILocationClient

    private lateinit var locationCoordinates: Map<String, Double>

    fun getLocationCoordinates(): Map<String, Double> { return locationCoordinates }

    override fun onBind(intent: Intent?): IBinder? { return null }

    override fun onCreate() {
        super.onCreate()
        locationClient = LocationClientImp(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
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
        serviceScope.cancel()
    }

    private fun start() {
        val notification = createNotification()

        locationClient.getLocationUpdates(1000L)
            .catch { e ->
                Log.e("LocationService", "Error getting location updates: ${e.printStackTrace()}")
            }
            .onEach { location ->
                this.locationCoordinates = mapOf(
                    "latitude" to location.latitude,
                    "longitude" to location.longitude
                )

                updateNotification(notification)

                /* TODO BORRAR*/
                Log.e("LocationService", "Location: ${location.latitude}, ${location.longitude}")
            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())
    }

    private fun stop() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotification(): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, LOCATION_NOTIFICATION_CHANNEL)
            .setContentTitle("Tracking your location...")
            .setContentText("Location Service is running")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)
    }

    private fun updateNotification(notification: NotificationCompat.Builder) {
        val updatedNotification = notification.setContentText("Your location has been set to: ${locationCoordinates["latitude"]}, ${locationCoordinates["longitude"]}")

        val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, updatedNotification.build())
    }
}