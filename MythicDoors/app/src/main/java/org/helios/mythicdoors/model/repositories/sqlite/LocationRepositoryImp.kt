package org.helios.mythicdoors.model.repositories.sqlite

import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import androidx.core.database.getStringOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.model.entities.Location
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.utils.connection.Connection
import org.helios.mythicdoors.utils.connection.Contracts
import java.time.LocalDate

class LocationRepositoryImp(dbHelper: Connection):
    IRepository<Location>,
    AutoCloseable
{
    private val dbWrite = dbHelper.writableDatabase
    private val dbRead = dbHelper.readableDatabase
    private val contentValues: ContentValues = ContentValues()
    private val userRepository: IRepository<User> = UserRepositoryImp(dbHelper)

    override fun close() {
        if (dbWrite.isOpen) dbWrite.close()
        if (dbRead.isOpen) dbRead.close()
    }

    override suspend fun getAll(): List<Location> = withContext(Dispatchers.IO) {
        val locationsList: MutableList<Location> = mutableListOf()

        try {
            dbRead.query(
                Contracts.LocationTableContract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
            ).use {cursor ->
                if (!cursor.moveToFirst()) return@withContext emptyList<Location>()
                with(cursor) {
                    do { locationsList.add(mapLocation(cursor))} while (moveToNext())
                }
            }
            return@withContext locationsList
        } catch (e: Exception) {
            Log.e("LocationRepositoryImp", "Error getting all locations: ${e.message}")
        }
        return@withContext emptyList<Location>()
    }

    override suspend fun getOne(id: Long): Location {
        TODO("Not yet implemented")
    }

    override suspend fun insertOne(item: Location): Long = withContext(Dispatchers.IO) {
        try {
            contentValues.clear()
            contentValues.putAll(buildLocation(item))
            return@withContext dbWrite.insert(Contracts.LocationTableContract.TABLE_NAME, null, contentValues)
        } catch (e: Exception) {
            Log.e("LocationRepositoryImp", "Error inserting location: ${e.message}")
        }
        return@withContext -1
    }

    override suspend fun updateOne(item: Location): Long = withContext(Dispatchers.IO) {
        try {
            contentValues.clear()
            contentValues.putAll(buildLocation(item))
            return@withContext dbWrite.update(
                Contracts.LocationTableContract.TABLE_NAME,
                contentValues,
                "${Contracts.LocationTableContract.COLUMN_NAME_ID} = ?",
                arrayOf(item.getId().toString())
            ).toLong()
        } catch (e: Exception) {
            Log.e("LocationRepositoryImp", "Error updating location: ${e.message}")
        }
        return@withContext -1
    }

    override suspend fun deleteOne(id: Long): Long {
        TODO("Not yet implemented")
    }

    override suspend fun count(): Int = withContext(Dispatchers.IO) {
        try {
            dbRead.query(
                Contracts.LocationTableContract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            ).use { cursor ->
                if (cursor.moveToFirst()) return@withContext cursor.count
            }
        } catch(e: Exception) {
            Log.e("LocationRepositoryImp", "Error counting locations: ${e.message}")
        }
        return@withContext 0
    }

    override suspend fun getLast(): Location = withContext(Dispatchers.IO) {
        try {
            dbRead.query(
                Contracts.LocationTableContract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "${Contracts.LocationTableContract.COLUMN_NAME_ID} DESC",
                "1"
            ).use { cursor ->
                cursor.moveToFirst()
                return@withContext mapLocation(cursor)
            }
        } catch (e: Exception) {
            Log.e("LocationRepositoryImp", "Error getting last location: ${e.message}")
        }
        return@withContext Location.createEmptyLocation()
    }

    private fun buildLocation(location: Location): ContentValues {
        return ContentValues().apply {
            if (location.getId() != null) put(Contracts.LocationTableContract.COLUMN_NAME_ID, location.getId())
            put(Contracts.LocationTableContract.COLUMN_NAME_ID_USER, location.getUser().getId())
            put(Contracts.LocationTableContract.COLUMN_NAME_LATITUDE, location.getLatitude())
            put(Contracts.LocationTableContract.COLUMN_NAME_LONGITUDE, location.getLongitude())
            put(Contracts.LocationTableContract.COLUMN_NAME_CREATED_AT, location.getCreatedAt().toString())
        }
    }

    private suspend fun mapLocation(cursor: Cursor): Location {
        return Location(
            cursor.getLong(cursor.getColumnIndexOrThrow(Contracts.LocationTableContract.COLUMN_NAME_ID)),
            getUser(cursor.getLong(cursor.getColumnIndexOrThrow(Contracts.LocationTableContract.COLUMN_NAME_ID_USER))),
            cursor.getDouble(cursor.getColumnIndexOrThrow(Contracts.LocationTableContract.COLUMN_NAME_LATITUDE)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(Contracts.LocationTableContract.COLUMN_NAME_LONGITUDE)),
            LocalDate.parse(cursor.getStringOrNull(cursor.getColumnIndex(Contracts.LocationTableContract.COLUMN_NAME_CREATED_AT)))
        )
    }

    private suspend fun getUser(id: Long): User {
        return userRepository.getOne(id)
    }
}
