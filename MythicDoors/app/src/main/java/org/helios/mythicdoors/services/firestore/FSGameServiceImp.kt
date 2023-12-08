package org.helios.mythicdoors.services.firestore

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.model.entities.Game
import org.helios.mythicdoors.model.repositories.firestore.FSGameRepositoryImp
import kotlin.Result.Companion.success

class FSGameServiceImp(
    private val gameRepository: FSGameRepositoryImp = FSGameRepositoryImp()
): IDataService<Game> {
    override suspend fun getAll(): List<Game>? = withContext(Dispatchers.IO) {
        return@withContext try {
            gameRepository.getAll().takeIf { it.isNotEmpty() }
        } catch (e: Exception) {
            Log.e("FSGameServiceImp", "Error getting all games: ${e.message}")
            null
        }
    }

    override suspend fun getOne(email: String): Game? = withContext(Dispatchers.IO) {
        return@withContext try {
            gameRepository.getOne(key = email).getOrNull()
        } catch (e: Exception) {
            Log.e("FSGameServiceImp", "Error getting game: ${e.message}")
            null
        }
    }

    override suspend fun saveOne(item: Game): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            item.takeIf { !it.isValid() }?.run {
                if (isFirestoreEmpty()) checkIfGameExists(this).takeIf { !it }?.let { gameRepository.insertOne(this) } ?: success(false)
                else gameRepository.updateOne(this)
            } ?: success(false)
        } catch (e: Exception) {
            Log.e("FSGameServiceImp", "Error inserting game: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun deleteOne(item: Game): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            if (checkIfGameExists(item)) gameRepository.deleteOne(item.getUser().getEmail())
            else success(false)
        } catch (e: Exception) {
            Log.e("FSGameServiceImp", "Error deleting game: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun count(): Int = withContext(Dispatchers.IO) {
        return@withContext try {
            gameRepository.count()
        } catch (e: Exception) {
            Log.e("FSGameServiceImp", "Error counting games: ${e.message}")
            0
        }
    }

    override suspend fun getLast(): Game? = withContext(Dispatchers.IO) {
        return@withContext try {
            gameRepository.getLast().takeIf { !it.isFirestoreEmpty() }
        } catch (e: Exception) {
            Log.e("FSGameServiceImp", "Error getting last game: ${e.message}")
            null
        }
    }

    private suspend fun checkIfGameExists(game: Game): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            gameRepository.getOne(game.getDocumentId() ?: return@withContext false).getOrNull() != null
        } catch (e: Exception) {
            false
        }
    }
}