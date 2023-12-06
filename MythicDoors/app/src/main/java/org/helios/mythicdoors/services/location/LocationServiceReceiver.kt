package org.helios.mythicdoors.services.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.helios.mythicdoors.MainActivity.Companion.dataController
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.Location
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.store.StoreManager

class LocationServiceReceiver : BroadcastReceiver() {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val store: StoreManager by lazy { StoreManager.getInstance() }
    private val context: Context? = store.getContext()
    private val player: User by lazy { store.getAppStore().actualUser ?: throw Exception("User not found") }

    override fun onReceive(context: Context?, intent: Intent?) {
        val locationService = LocationService.instance

        intent?.action?.let {
            when (it) {
                LocationService.ACTION_LOCATION_UPDATE -> {
                    Log.e("GameLogicViewModel", "onReceive: ACTION_LOCATION_UPDATE")
                    val lastLocation = locationService.locationLiveData.value
                    lastLocation?.let {
                        Log.e("GameLogicViewModel", "onReceive: $lastLocation")
                        scope.launch {
                        }
                    }

                    stopLocationService()
                }

                    else -> { stopLocationService() }
            }
        }
    }

    private fun stopLocationService() {
        val intent = Intent(context, LocationService::class.java)
        intent.action = LocationService.ACTION_STOP
        context?.startService(intent)
    }
}