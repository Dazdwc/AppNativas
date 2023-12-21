package org.helios.mythicdoors.services.api

import org.helios.mythicdoors.model.entities.Jackpot

interface IJackpotService {
    suspend fun getJackpots(): Result<List<Jackpot>>
    suspend fun getLastJackpot(): Result<Jackpot>
    suspend fun postJackpot(jackpot: Jackpot): Result<String>
    suspend fun putJackpot(jackpotId: String, jackpot: Jackpot): Result<String>
}