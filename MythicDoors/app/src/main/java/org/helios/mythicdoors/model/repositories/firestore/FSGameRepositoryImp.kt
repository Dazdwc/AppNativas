package org.helios.mythicdoors.model.repositories.firestore

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.model.entities.Game
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.utils.connection.firestone.Contracts.GamesDocumentContract
import org.helios.mythicdoors.utils.connection.firestone.FirestoreClient
import org.helios.mythicdoors.utils.connection.firestone.FirestoreCollection
import java.time.LocalDateTime

class FSGameRepositoryImp(): IRepository<Game> {
    private val firestoneClient: FirestoreClient = FirestoreClient.getInstance()
    private val gamesCollection by lazy { firestoneClient.getCollection(FirestoreCollection.GAMES) }

    override suspend fun getAll(): List<Game> = withContext(Dispatchers.IO) {
        val gamesList: MutableList<Game> = mutableListOf()

        return@withContext try {
            val snapshot = gamesCollection.get().await()
            snapshot.documents.map {
                val game: Game = mapGame(it.data ?: throw GamesRepositoryException("Error getting all games"))
                gamesList.add(game)
            }
            gamesList
        } catch (e: Exception) {
            Log.e("FSGameRepositoryImp", "Error getting all games: ${e.message}")
            emptyList<Game>()
        }
    }

    override suspend fun getOne(key: String): Result<Game> = withContext(Dispatchers.IO) {
        return@withContext try {
            val snapshot = gamesCollection.whereEqualTo(GamesDocumentContract.FIELD_NAME_ID, key).get().await()
            val game: Game = mapGame(snapshot.documents.first().data ?: throw GamesRepositoryException("Error getting game"))
            Result.success(game)
        } catch (e: Exception) {
            Log.e("FSGameRepositoryImp", "Error getting game: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun insertOne(item: Game): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            gamesCollection.document(item.getUser().getEmail()).set(buildGame(item)).await()
            Result.success(true)
        } catch (e: Exception) {
            Log.e("FSGameRepositoryImp", "Error inserting game: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun updateOne(item: Game): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            gamesCollection.document(item.getUser().getEmail()).update(buildGame(item)).await()
            Result.success(true)
        } catch (e: Exception) {
            Log.e("FSGameRepositoryImp", "Error updating game: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun deleteOne(key: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            gamesCollection.document(key).delete().await()
            Result.success(true)
        } catch (e: Exception) {
            Log.e("FSGameRepositoryImp", "Error deleting game: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun count(): Int = withContext(Dispatchers.IO) {
        return@withContext try {
            val snapshot = gamesCollection.get().await()
            snapshot.size()
        } catch (e: Exception) {
            Log.e("FSGameRepositoryImp", "Error counting games: ${e.message}")
            0
        }
    }

    override suspend fun getLast(): Game = withContext(Dispatchers.IO) {
        return@withContext try {
            val snapshot = gamesCollection.orderBy(GamesDocumentContract.FIELD_NAME_GAME_DATE_TIME).limitToLast(1).get().await()
            val game: Game = mapGame(snapshot.documents.first().data ?: throw GamesRepositoryException("Error getting last game"))
            game
        } catch (e: Exception) {
            Log.e("FSGameRepositoryImp", "Error getting last game: ${e.message}")
            Game.createEmptyGame()
        }
    }

    private fun buildGame(item: Game): Map<String, Any?> {
        return hashMapOf(
            GamesDocumentContract.FIELD_NAME_USER to item.getUser().toString(),
            GamesDocumentContract.FIELD_NAME_COIN to item.getCoin().toString(),
            GamesDocumentContract.FIELD_NAME_LEVEL to item.getLevel().toString(),
            GamesDocumentContract.FIELD_NAME_SCORE to item.getScore().toString(),
            GamesDocumentContract.FIELD_NAME_MAX_ENEMY_LEVEL to item.getMaxEnemyLevel().toString(),
            GamesDocumentContract.FIELD_NAME_GAME_DATE_TIME to item.getDateTime().toString()
        )
    }

    private fun mapGame(item: Map<String, Any?>): Game {
        return Game.create(
            documentId = item[GamesDocumentContract.FIELD_NAME_ID] as String?,
            user = item[GamesDocumentContract.FIELD_NAME_USER] as User,
            coin = item[GamesDocumentContract.FIELD_NAME_COIN] as Int,
            level = item[GamesDocumentContract.FIELD_NAME_LEVEL] as Int,
            score = item[GamesDocumentContract.FIELD_NAME_SCORE] as Int,
            maxEnemyLevel = item[GamesDocumentContract.FIELD_NAME_MAX_ENEMY_LEVEL] as Int
        ).apply {
            setDateTime(item[GamesDocumentContract.FIELD_NAME_GAME_DATE_TIME] as LocalDateTime)
        }
    }
}

class GamesRepositoryException(message: String): Exception(message)