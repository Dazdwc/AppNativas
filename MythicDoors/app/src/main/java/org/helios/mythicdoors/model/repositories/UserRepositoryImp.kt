package org.helios.mythicdoors.model.repositories

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.utils.Connection
import org.helios.mythicdoors.utils.Contracts
import java.time.LocalDate

/* Autoclasable permite que la conexión a la base de datos se cierre automáticamente sin tener que manejarlo manualmente y nos ahorra código además de que es más seguro
y aumenta su rendimiento y legibilidad.
*/
class UserRepositoryImp(dbHelper: Connection):
    IRepository<User>,
    AutoCloseable
{
    private val dbWrite: SQLiteDatabase by lazy { dbHelper.writableDatabase }
    private val dbRead: SQLiteDatabase by lazy { dbHelper.readableDatabase }
    private var contentValues: ContentValues = ContentValues()

    override fun close() {
        if (dbWrite.isOpen) dbWrite.close()
        if (dbRead.isOpen) dbRead.close()
    }

    override suspend fun getAll(): List<User> = withContext(Dispatchers.IO) {
        val usersList: MutableList<User> = mutableListOf()
        try {
            dbRead.query(
                Contracts.UserTableContract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
            ).use {cursor ->
                if (!cursor.moveToFirst()) return@withContext emptyList<User>()
                with(cursor) {
                    do { usersList.add(mapUser(cursor))} while (moveToNext())
                }
            }
        } catch (e: Exception) {
            Log.e("UserRepositoryImp", "Error getting all users: ${e.message}")
        }
        return@withContext usersList
    }

    override suspend fun getOne(id: Long): User = withContext(Dispatchers.IO) {
        try {
            dbRead.query(
                Contracts.UserTableContract.TABLE_NAME,
                null,
                "${Contracts.UserTableContract.COLUMN_NAME_ID} = ?",
                arrayOf(id.toString()),
                null,
                null,
                null,
                null,
            ).use { cursor ->
                if (cursor.moveToFirst()) return@withContext mapUser(cursor)
            }
        } catch(e: Exception) {
            Log.e("UserRepositoryImp", "Error getting user: ${e.message}")
        }
        return@withContext User.createEmptyUser()
    }

    override suspend fun insertOne(item: User): Long = withContext(Dispatchers.IO) {
        try {
            contentValues= buildUser(item)
            return@withContext dbWrite.insert(Contracts.UserTableContract.TABLE_NAME, null, contentValues)
        } catch (e: Exception) {
            Log.e("UserRepositoryImp", "Error inserting user: ${e.message}")
            return@withContext  -1
        }
    }

    override suspend fun updateOne(item: User): Long = withContext(Dispatchers.IO) {
        try {
            contentValues = buildUser(item)
            return@withContext dbWrite.update(
                Contracts.UserTableContract.TABLE_NAME,
                contentValues,
                "${Contracts.UserTableContract.COLUMN_NAME_ID} = ?",
                arrayOf(item.getId().toString())
            ).toLong()
        } catch(e: Exception) {
            Log.e("UserRepositoryImp", "Error updating user: ${e.message}")
            return@withContext -1
        }
    }

    override suspend fun deleteOne(id: Long): Long = withContext(Dispatchers.IO) {
        try {
            return@withContext dbWrite.delete(
                Contracts.UserTableContract.TABLE_NAME,
                "${Contracts.UserTableContract.COLUMN_NAME_ID} = ?",
                arrayOf(id.toString())
            ).toLong()
        } catch(e: Exception) {
            Log.e("UserRepositoryImp", "Error deleting user: ${e.message}")
            return@withContext -1
        }
    }

    override suspend fun getLast(): User = withContext(Dispatchers.IO) {
        try {
            dbRead.query(
                Contracts.UserTableContract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "${Contracts.UserTableContract.COLUMN_NAME_ID} DESC",
                "1"
            ).use { cursor ->
                if (cursor.moveToFirst()) return@withContext mapUser(cursor)
            }
        } catch(e: Exception) {
            Log.e("UserRepositoryImp", "Error getting last user: ${e.message}")
        }
        return@withContext User.createEmptyUser()
    }

    override suspend fun count(): Int = withContext(Dispatchers.IO) {
        try {
            dbRead.query(
                Contracts.UserTableContract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
            ).use { cursor ->
                if (cursor.moveToFirst()) return@withContext cursor.count
            }
        } catch(e: Exception) {
            Log.e("UserRepositoryImp", "Error counting users: ${e.message}")
            return@withContext -1
        }
        return@withContext 0
    }

    private fun buildUser(user: User): ContentValues {
        return ContentValues().apply {
            if (user.getId() != null) put(Contracts.UserTableContract.COLUMN_NAME_ID, user.getId())
            put(Contracts.UserTableContract.COLUMN_NAME_NAME, user.getName())
            put(Contracts.UserTableContract.COLUMN_NAME_EMAIL, user.getEmail())
            put(Contracts.UserTableContract.COLUMN_NAME_PASSWORD, user.getPassword())
            put(Contracts.UserTableContract.COLUMN_NAME_SCORE, user.getScore())
            put(Contracts.UserTableContract.COLUMN_NAME_LEVEL, user.getLevel())
            put(Contracts.UserTableContract.COLUMN_NAME_EXPERIENCE, user.getExperience())
            put(Contracts.UserTableContract.COLUMN_NAME_COINS, user.getCoins())
            put(Contracts.UserTableContract.COLUMN_NAME_GOLD_COINS, user.getGoldCoins())
            put(Contracts.UserTableContract.COLUMN_NAME_IS_ACTIVE, if (user.getIsActive()) 1 else 0)
            put(Contracts.UserTableContract.COLUMN_NAME_CREATED_AT, user.getCreatedAt().toString())
        }
    }

    private fun mapUser(cursor: Cursor): User {
        return User(
            cursor.getLong(cursor.getColumnIndexOrThrow(Contracts.UserTableContract.COLUMN_NAME_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(Contracts.UserTableContract.COLUMN_NAME_NAME)),
            cursor.getString(cursor.getColumnIndexOrThrow(Contracts.UserTableContract.COLUMN_NAME_EMAIL)),
            cursor.getString(cursor.getColumnIndexOrThrow(Contracts.UserTableContract.COLUMN_NAME_PASSWORD)),
            cursor.getInt(cursor.getColumnIndexOrThrow(Contracts.UserTableContract.COLUMN_NAME_SCORE)),
            cursor.getInt(cursor.getColumnIndexOrThrow(Contracts.UserTableContract.COLUMN_NAME_LEVEL)),
            cursor.getInt(cursor.getColumnIndexOrThrow(Contracts.UserTableContract.COLUMN_NAME_EXPERIENCE)),
            cursor.getInt(cursor.getColumnIndexOrThrow(Contracts.UserTableContract.COLUMN_NAME_COINS)),
            cursor.getInt(cursor.getColumnIndexOrThrow(Contracts.UserTableContract.COLUMN_NAME_GOLD_COINS)),
            cursor.getInt(cursor.getColumnIndexOrThrow(Contracts.UserTableContract.COLUMN_NAME_IS_ACTIVE)) == 1,
            LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(Contracts.UserTableContract.COLUMN_NAME_CREATED_AT)))
        )
    }
}