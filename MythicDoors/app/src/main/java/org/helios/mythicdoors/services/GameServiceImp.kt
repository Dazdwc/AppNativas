package org.helios.mythicdoors.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.model.entities.Game
import org.helios.mythicdoors.model.repositories.GameRepositoryImp
import org.helios.mythicdoors.model.repositories.IRepository
import org.helios.mythicdoors.services.interfaces.IGameService
import org.helios.mythicdoors.utils.Connection

class GameServiceImp(dbHelper: Connection): IGameService {
    private var repository: IRepository<Game>

    init { repository = GameRepositoryImp(dbHelper) }

    override suspend fun getGames(): List<Game>? = withContext(Dispatchers.IO) {
        try {
            return@withContext repository.getAll().takeIf { it.isNotEmpty() }
        } catch(e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    override suspend fun getGame(id: Long): Game? = withContext(Dispatchers.IO) {
        try {
            return@withContext repository.getOne(id).takeIf { !it.isEmpty() }
        } catch(e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    override suspend fun getLastGame(): Game? = withContext(Dispatchers.IO){
        try {
            return@withContext repository.getLast().takeIf { !it.isEmpty() }
        } catch(e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    override suspend fun saveGame(game: Game): Boolean = withContext(Dispatchers.IO) {
        try {
            game.takeIf { it.isValid() }?.let { return@withContext repository.insertOne(game) > 0 } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext false
        }
    }

    override suspend fun deleteGame(id: Long): Boolean = withContext(Dispatchers.IO){
        try {
            if (id > 0) return@withContext getGame(id).takeIf { it != null }?.let { repository.deleteOne(id) > 0 } ?: false
        } catch(e: Exception) {
            e.printStackTrace()
        }
        return@withContext false
    }

    override suspend fun countGames(): Int = withContext(Dispatchers.IO) {
        try {
            return@withContext getGames()?.size ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext -1
    }
}