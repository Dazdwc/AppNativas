package org.helios.mythicdoors.utils.api

import org.helios.mythicdoors.model.entities.Jackpot
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @GET("jackpots.json")
    suspend fun getJackpotsCall(): Response<Map<String, *>>

    @POST("jackpots.json")
    suspend fun postJackpotCall(@Body jackpot: Jackpot): Response<Map<String, String>>

    @PUT("jackpots/{jackpotId}.json")
    suspend fun putJackpotCall(jackpotId: String, @Body jackpot: Jackpot): Response<Map<String, String>>
}