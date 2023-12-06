package org.helios.mythicdoors.services.interfaces

import org.helios.mythicdoors.model.entities.Location

interface ILocationService {
    suspend fun getLocations(): List<Location>?
    suspend fun saveLocation(location: Location): Boolean
    suspend fun getLastLocation(): Location?
}