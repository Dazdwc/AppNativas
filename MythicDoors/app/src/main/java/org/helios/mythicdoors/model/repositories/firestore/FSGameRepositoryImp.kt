package org.helios.mythicdoors.model.repositories.firestore

import android.util.Log
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.model.entities.Game
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.utils.connection.firestone.Contracts.GamesDocumentContract
import org.helios.mythicdoors.utils.connection.firestone.FirestoreClient
import org.helios.mythicdoors.utils.connection.firestone.FirestoreCollection
import java.time.LocalDateTime
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

class FSGameRepositoryImp(): IRepository<Game> {

    private val gamesCollection = FirestoreClient.getInstance().getCollection(FirestoreCollection.GAMES)

    override suspend fun getAll(): List<Game> = withContext(Dispatchers.IO) {
        val gamesList: MutableList<Game> = mutableListOf()

        return@withContext try {
            val snapshot = gamesCollection
                .get()
                .addOnFailureListener { e ->
                    Log.e("FSGameRepositoryImp", "Error getting all games: ${e.message}")
                }
                .await()
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
            val snapshot = gamesCollection.whereEqualTo(GamesDocumentContract.FIELD_NAME_ID, key)
                .get()
                .addOnFailureListener { e ->
                    Log.d("FSGameRepositoryImp", "Cannot get the game: ${e.message}")
                }
                .await()
            val game: Game = mapGame(snapshot.documents.first().data ?: throw GamesRepositoryException("Error getting game"))
            success(game)
        } catch (e: Exception) {
            Log.e("FSGameRepositoryImp", "Error getting game: ${e.message}")
            failure(e)
        }
    }

    override suspend fun insertOne(item: Game): Result<Boolean> = withContext(Dispatchers.IO) {
        var success: Result<Boolean> = success(false)

        return@withContext try {
            gamesCollection.document()
                .set(buildGame(item))
                .addOnSuccessListener {
                    success = success(true)
                }
                .addOnFailureListener { e ->
                    Log.e("FSGameRepositoryImp", "Error inserting game: ${e.message}")
                    success = success(false)
                }
                .await()
            success
        } catch (e: Exception) {
            Log.e("FSGameRepositoryImp", "Error inserting game: ${e.message}")
            failure(e)
        }
    }

    override suspend fun updateOne(item: Game): Result<Boolean> = withContext(Dispatchers.IO) {
        var success: Result<Boolean> = success(false)

        return@withContext try {
            gamesCollection.document(item.getUser().getEmail())
                .update(buildGame(item))
                .addOnSuccessListener {
                    success = success(true)
                }
                .addOnFailureListener { e ->
                    Log.e("FSGameRepositoryImp", "Error updating game: ${e.message}")
                    success = success(false)
                }
                .await()
            success
        } catch (e: Exception) {
            Log.e("FSGameRepositoryImp", "Error updating game: ${e.message}")
            failure(e)
        }
    }

    override suspend fun deleteOne(key: String): Result<Boolean> = withContext(Dispatchers.IO) {
        var success: Result<Boolean> = success(false)

        return@withContext try {
            gamesCollection.document(key)
                .delete()
                .addOnSuccessListener {
                    success = success(true)
                }
                .addOnFailureListener { e ->
                    Log.e("FSGameRepositoryImp", "Error deleting game: ${e.message}")
                    success = success(false)
                }
                .await()
            success
        } catch (e: Exception) {
            Log.e("FSGameRepositoryImp", "Error deleting game: ${e.message}")
            failure(e)
        }
    }

    override suspend fun count(): Int = withContext(Dispatchers.IO) {
        return@withContext try {
            gamesCollection.get().await().size()
        } catch (e: Exception) {
            Log.e("FSGameRepositoryImp", "Error counting games: ${e.message}")
            0
        }
    }

    override suspend fun getLast(): Result<Game> = withContext(Dispatchers.IO) {
        return@withContext try {
            val snapshot: QuerySnapshot = gamesCollection
                .orderBy(GamesDocumentContract.FIELD_NAME_GAME_DATE_TIME)
                .limitToLast(1)
                .get()
                .addOnFailureListener { e ->
                    Log.e("FSGameRepositoryImp", "Error getting last game: ${e.message}")
                }
                .await()

            val game: Game = mapGame(snapshot.documents.first().data ?: throw GamesRepositoryException("Error getting last game"))
            success(game)
        } catch (e: Exception) {
            Log.e("FSGameRepositoryImp", "Error getting last game: ${e.message}")
            failure(e)
        }
    }

    private fun buildGame(item: Game): Map<String, Any?> {
        return hashMapOf(
            GamesDocumentContract.FIELD_NAME_USER to item.getUser(),
            GamesDocumentContract.FIELD_NAME_COIN to item.getCoin(),
            GamesDocumentContract.FIELD_NAME_LEVEL to item.getLevel(),
            GamesDocumentContract.FIELD_NAME_SCORE to item.getScore(),
            GamesDocumentContract.FIELD_NAME_MAX_ENEMY_LEVEL to item.getMaxEnemyLevel(),
            GamesDocumentContract.FIELD_NAME_GAME_DATE_TIME to item.getDateTime()
        )
    }

    private fun mapGame(item: Map<String, Any?>): Game {
        return Game.create(
            user = item[GamesDocumentContract.FIELD_NAME_USER] as User,
            coin = item[GamesDocumentContract.FIELD_NAME_COIN] as Long,
            level = item[GamesDocumentContract.FIELD_NAME_LEVEL] as Long,
            score = item[GamesDocumentContract.FIELD_NAME_SCORE] as Long,
            maxEnemyLevel = item[GamesDocumentContract.FIELD_NAME_MAX_ENEMY_LEVEL] as Long
        ).apply {
            setDateTime(item[GamesDocumentContract.FIELD_NAME_GAME_DATE_TIME] as LocalDateTime)
        }
    }
}

class GamesRepositoryException(message: String): Exception(message)