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
    companion object {
        @Volatile
        private var instance: FSGameServiceImp? = null

        fun getInstance(): FSGameServiceImp {
            return instance ?: synchronized(this) {
                instance ?: buildFSGameServiceImp().also { instance = it }
            }
        }

        private fun buildFSGameServiceImp(): FSGameServiceImp {
            return FSGameServiceImp()
        }
    }

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
            item.takeIf { it.isValid() }?.run {
                Log.d("FSGameServiceImp", "Saving game: $item")
                if (isNotEmpty()) checkIfGameExists(this).takeIf { !it }?.let { gameRepository.insertOne(this) } ?: success(false)
                else success(false)
            } ?: success(false)
        } catch (e: Exception) {
            Log.e("FSGameServiceImp", "Error inserting game: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun updateOne(item: Game): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            if (checkIfGameExists(item)) gameRepository.updateOne(item)
            else success(false)
        } catch (e: Exception) {
            Log.e("FSGameServiceImp", "Error updating game: ${e.message}")
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
            gameRepository.getLast().getOrNull()
        } catch (e: Exception) {
            Log.e("FSGameServiceImp", "Error getting last game: ${e.message}")
            null
        }
    }

    private suspend fun checkIfGameExists(game: Game): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            gameRepository.getAll().takeIf { it.isNotEmpty() }?.contains(game) ?: false
        } catch (e: Exception) {
            false
        }
    }
}