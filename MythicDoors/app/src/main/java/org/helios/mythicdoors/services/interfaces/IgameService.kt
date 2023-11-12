package org.helios.mythicdoors.services.interfaces

import org.helios.mythicdoors.model.entities.Game

interface IGameService {
    suspend fun getGames(): List<Game>?
    suspend fun getGame(id: Long): Game?
    suspend fun saveGame(game: Game): Boolean
    suspend fun deleteGame(id: Long): Boolean
    suspend fun countGames(): Int
    suspend fun getLastGame(): Game?
}