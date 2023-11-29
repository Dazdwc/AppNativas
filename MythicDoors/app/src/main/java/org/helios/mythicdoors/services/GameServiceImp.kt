package org.helios.mythicdoors.services

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.model.entities.Game
import org.helios.mythicdoors.model.repositories.GameRepositoryImp
import org.helios.mythicdoors.model.repositories.IRepository
import org.helios.mythicdoors.services.interfaces.IGameService
import org.helios.mythicdoors.utils.connection.Connection

class GameServiceImp(dbHelper: Connection): IGameService {
    private val repository: IRepository<Game>

    init { repository = GameRepositoryImp(dbHelper) }

    override suspend fun getGames(): List<Game>? = withContext(Dispatchers.IO) {
        try {
            return@withContext repository.getAll().takeIf { it.isNotEmpty() }
        } catch(e: Exception) {
            Log.e("GameServiceImp", "Error getting all games: ${e.message}")
            return@withContext null
        }
    }

    override suspend fun getGame(id: Long): Game? = withContext(Dispatchers.IO) {
        try {
            return@withContext repository.getOne(id).takeIf { !it.isEmpty() }
        } catch(e: Exception) {
            Log.e("GameServiceImp", "Error getting game: ${e.message}")
            return@withContext null
        }
    }

    override suspend fun getLastGame(): Game? = withContext(Dispatchers.IO){
        try {
            return@withContext repository.getLast().takeIf { !it.isEmpty() }
        } catch(e: Exception) {
            Log.e("GameServiceImp", "Error getting last game: ${e.message}")
            return@withContext null
        }
    }

    override suspend fun saveGame(game: Game): Boolean = withContext(Dispatchers.IO) {
        try {
            game.takeIf { it.isValid() }?.let { return@withContext repository.insertOne(game) > 0 } ?: false
        } catch (e: Exception) {
            Log.e("GameServiceImp", "Error saving game: ${e.message}")
            return@withContext false
        }
    }

    override suspend fun deleteGame(id: Long): Boolean = withContext(Dispatchers.IO){
        try {
            if (id > 0) return@withContext getGame(id).takeIf { it != null }?.let { repository.deleteOne(id) > 0 } ?: false
        } catch(e: Exception) {
            Log.e("GameServiceImp", "Error deleting game: ${e.message}")
        }
        return@withContext false
    }

    override suspend fun countGames(): Int = withContext(Dispatchers.IO) {
        try {
            return@withContext getGames()?.size ?: 0
        } catch (e: Exception) {
            Log.e("GameServiceImp", "Error counting games: ${e.message}")
        }
        return@withContext -1
    }
}