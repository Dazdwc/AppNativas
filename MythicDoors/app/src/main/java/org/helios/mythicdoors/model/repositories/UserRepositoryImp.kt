package org.helios.mythicdoors.model.repositories

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.utils.Connection
import org.helios.mythicdoors.utils.Contracts

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

    override suspend fun getAll(): List<User> = withContext(Dispatchers.IO) {
        dbRead.use {db ->
            try {
                db.query(
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
                        do { /* TODO: Map User */ } while (moveToNext())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        TODO("Not yet implemented")
    }

    override suspend fun getOne(id: Long): User {
        TODO("Not yet implemented")
    }

    override suspend fun count(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getLast(): User {
        TODO("Not yet implemented")
    }

    override suspend fun deleteOne(item: User): Long {
        TODO("Not yet implemented")
    }

    override suspend fun updateOne(item: User): Long {
        TODO("Not yet implemented")
    }

    override suspend fun insertOne(item: User): Long {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }

    private fun buildUser(user: User): ContentValues {
        return contentValues.apply {}


    }

    private fun mapUser(cursor: Cursor): User {
        return User()
    }
}