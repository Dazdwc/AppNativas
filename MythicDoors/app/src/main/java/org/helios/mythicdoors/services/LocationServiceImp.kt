package org.helios.mythicdoors.services

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.model.entities.Location
import org.helios.mythicdoors.model.repositories.sqlite.IRepository
import org.helios.mythicdoors.model.repositories.sqlite.LocationRepositoryImp
import org.helios.mythicdoors.services.interfaces.ILocationService
import org.helios.mythicdoors.utils.connection.Connection

class LocationServiceImp(dbHelper: Connection): ILocationService {
    private val repository: IRepository<Location>

    init { repository = LocationRepositoryImp(dbHelper) }

    override suspend fun getLocations(): List<Location>? = withContext(Dispatchers.IO) {
        try {
            return@withContext repository.getAll().takeIf { it.isNotEmpty() }
        } catch(e: Exception) {
            Log.e("LocationServiceImp", "Error getting all locations: ${e.message}")
            return@withContext null
        }
    }

    override suspend fun saveLocation(location: Location): Boolean = withContext(Dispatchers.IO) {
        try {
            location.takeIf { it.isValid() }?.let {
                return@withContext repository.insertOne(location) > 0
            } ?: false
        } catch (e: Exception) {
            Log.e("LocationServiceImp", "Error saving location: $e")
            return@withContext false
        }
    }

    override suspend fun getLastLocation(): Location? = withContext(Dispatchers.IO){
        try {
            return@withContext repository.getLast().takeIf { !it.isEmpty() }
        } catch(e: Exception) {
            Log.e("LocationServiceImp", "Error getting last location: ${e.stackTrace}")
            return@withContext null
        }
    }
}