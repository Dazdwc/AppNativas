package org.helios.mythicdoors.services.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface ILocationClient {
    fun getLocationUpdates(interval: Long): Flow<Location>

    class LocationException(message: String): Exception(message)
}