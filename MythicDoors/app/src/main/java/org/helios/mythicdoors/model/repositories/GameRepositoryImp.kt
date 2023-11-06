package org.helios.mythicdoors.model.repositories

import android.content.ContentValues
import android.database.Cursor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.model.entities.Game
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.utils.Connection
import org.helios.mythicdoors.utils.Contracts
import java.time.LocalDateTime

class GameRepositoryImp(dbHelper: Connection):
    IRepository<Game>,
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

    override suspend fun getAll(): List<Game> = withContext(Dispatchers.IO) {
        val gamesList: MutableList<Game> = mutableListOf()

        dbRead.use {db ->
            try {
                db.query(
                    Contracts.GameTableContract.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                ).use {cursor ->
                    if (!cursor.moveToFirst()) return@withContext emptyList<Game>()
                    with(cursor) {
                        do { gamesList.add(mapGame(cursor))} while (moveToNext())
                    }
                    return@withContext gamesList
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return@withContext emptyList<Game>()
    }

    override suspend fun getOne(id: Long): Game = withContext(Dispatchers.IO) {
        dbRead.use { db ->
            try {
                db.query(
                    Contracts.GameTableContract.TABLE_NAME,
                    null,
                    "${Contracts.GameTableContract.COLUMN_NAME_ID} = ?",
                    arrayOf(id.toString()),
                    null,
                    null,
                    null,
                    null,
                ).use { cursor ->
                    if (cursor.moveToFirst()) return@withContext mapGame(cursor)
                }
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }
        return@withContext Game.createEmptyGame()
    }

    override suspend fun insertOne(item: Game): Long = withContext(Dispatchers.IO) {
        dbWrite.use { db ->
            try{
                contentValues.clear()
                contentValues.putAll(buildGame(item))
                return@withContext db.insert(Contracts.GameTableContract.TABLE_NAME, null, contentValues)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return@withContext -1
    }

    override suspend fun updateOne(item: Game): Long = withContext(Dispatchers.IO) {
        dbWrite.use { db ->
            try {
                contentValues.clear()
                contentValues.putAll(buildGame(item))
                return@withContext db.update(
                    Contracts.GameTableContract.TABLE_NAME,
                    contentValues,
                    "${Contracts.GameTableContract.COLUMN_NAME_ID} = ?",
                    arrayOf(item.getId().toString())
                ).toLong()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return@withContext -1
    }

    override suspend fun deleteOne(id: Long): Long = withContext(Dispatchers.IO) {
        dbWrite.use { db ->
            try {
                return@withContext db.delete(
                    Contracts.GameTableContract.TABLE_NAME,
                    "${Contracts.GameTableContract.COLUMN_NAME_ID} = ?",
                    arrayOf(id.toString())
                ).toLong()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return@withContext -1
    }

    override suspend fun getLast(): Game = withContext(Dispatchers.IO) {
        dbRead.use { db ->
            try {
                db.query(
                    Contracts.GameTableContract.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "${Contracts.GameTableContract.COLUMN_NAME_ID} DESC",
                    "1"
                ).use { cursor ->
                    if (cursor.moveToFirst()) return@withContext mapGame(cursor)
                }
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }
        return@withContext Game.createEmptyGame()
    }

    override suspend fun count(): Int = withContext(Dispatchers.IO)
    {
        dbRead.use { db ->
            try {
                db.query(
                    Contracts.GameTableContract.TABLE_NAME,
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
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext -1
            }
        }
        return@withContext 0
    }

    private fun buildGame(game: Game): ContentValues {
        return ContentValues().apply {
            if (game.getId() != null) put(Contracts.GameTableContract.COLUMN_NAME_ID, game.getId())
            put(Contracts.GameTableContract.COLUMN_NAME_ID_USER, game.getUser().getId())
            put(Contracts.GameTableContract.COLUMN_NAME_LEVEL, game.getLevel())
            put(Contracts.GameTableContract.COLUMN_NAME_COIN, game.getCoin())
            put(Contracts.GameTableContract.COLUMN_NAME_SCORE, game.getScore())
            put(Contracts.GameTableContract.COLUMN_NAME_MAX_ENEMY_LEVEL, game.getMaxEnemyLevel())
            put(Contracts.GameTableContract.COLUMN_NAME_GAME_DATE_TIME, game.getDateTime().toString())
        }
    }

    private suspend fun mapGame(cursor: Cursor): Game {
        return Game(
            cursor.getLong(cursor.getColumnIndexOrThrow(Contracts.GameTableContract.COLUMN_NAME_ID)),
            getUser(cursor.getLong(cursor.getColumnIndexOrThrow(Contracts.GameTableContract.COLUMN_NAME_ID_USER))),
            cursor.getInt(cursor.getColumnIndexOrThrow(Contracts.GameTableContract.COLUMN_NAME_COIN)),
            cursor.getInt(cursor.getColumnIndexOrThrow(Contracts.GameTableContract.COLUMN_NAME_LEVEL)),
            cursor.getInt(cursor.getColumnIndexOrThrow(Contracts.GameTableContract.COLUMN_NAME_SCORE)),
            cursor.getInt(cursor.getColumnIndexOrThrow(Contracts.GameTableContract.COLUMN_NAME_MAX_ENEMY_LEVEL)),
            LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(Contracts.GameTableContract.COLUMN_NAME_GAME_DATE_TIME))),
        )
    }

    private suspend fun getUser(id: Long): User {
        return userRepository.getOne(id)
    }
}