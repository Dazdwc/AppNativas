package org.helios.mythicdoors.model.repositories.firestore

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val adapter = moshi.adapter(Game::class.java)

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

    override suspend fun getOne(key: String): Result<Game?> = withContext(Dispatchers.IO) {
        return@withContext try {
            val snapshot = gamesCollection.whereEqualTo(GamesDocumentContract.FIELD_NAME_ID, key)
                .get()
                .addOnFailureListener { e ->
                    Log.d("FSGameRepositoryImp", "Cannot get the game: ${e.message}")
                }
                .await()

            if (snapshot.documents.isEmpty()) return@withContext success(null)

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
        return adapter.toJson(item).let {
            moshi.adapter<Map<String, Any?>>(Map::class.java).fromJson(it) ?: throw GamesRepositoryException("Error building game")
        }
    }

    private fun mapGame(item: Map<String, Any?>): Game {
        return adapter.fromJson(moshi.adapter<Map<String, Any?>>(Map::class.java).toJson(item) ?: throw GamesRepositoryException("Error mapping game"))
            ?: throw GamesRepositoryException("Error mapping game")
    }
}

class GamesRepositoryException(message: String): Exception(message)