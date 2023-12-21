package org.helios.mythicdoors.services.api

import android.util.Log
import com.google.firebase.Timestamp
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.Buffer
import org.helios.mythicdoors.model.entities.Jackpot
import org.helios.mythicdoors.utils.api.RetrofitClient
import retrofit2.Response
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

class JackpotApiService: IJackpotService {
    companion object {
        @Volatile
        private var instance: JackpotApiService? = null

        fun getInstance(): JackpotApiService {
            return instance ?: synchronized(this) {
                instance ?: buildJackpotApiService().also { instance = it }
            }
        }

        private fun buildJackpotApiService(): JackpotApiService {
            return JackpotApiService()
        }
    }

    private val apiService = RetrofitClient.instance

    override suspend fun getJackpots(): Result<List<Jackpot>> = withContext(Dispatchers.IO) {
        val jackpotsList: MutableList<Jackpot> = mutableListOf()
        val moshi = buildMoshiJackpotTreeMapReader()
        val jsonAdapter = buildMoshiJackpotTreeMapAdapter(moshi)

        return@withContext try {
            val response = apiService.getJackpotsCall()
            response.handleExceptions()
                .onSuccess {
                    response.body()?.let {jackpotsTreeMap ->
                        val jsonReader = JsonReader.of(Buffer().writeUtf8(jackpotsTreeMap.toString())).apply { isLenient = true }
                        jsonAdapter.fromJson(jsonReader)?.forEach  { entry ->
                            val value = entry.value as Map<*, *>

                            val jackpot = Jackpot(
                                pot = (value["pot"] as Double).toLong(),
                                timestamp = Timestamp(((value["timestamp"] as Map<*, *>)["seconds"] as Double).toLong(), ((value["timestamp"] as Map<*, *>)["nanoseconds"] as Double).toInt())
                            )
                            jackpotsList.add(jackpot)
                            }
                        } ?: throw JackpotApiServiceException("${ErrorCodes.BAD_REQUEST} - ${ErrorMessages.BAD_REQUEST}: Error getting jackpots")

                    return@withContext success(jackpotsList)
                    }
                    return@withContext success(emptyList<Jackpot>())
                .onFailure {
                    val message =  response.errorBody()?.string() ?: ErrorMessages.UNDEFINED_ERROR
                    throw JackpotApiServiceException("Error getting jackpots: $message")
                }
        } catch (e: Exception) {
            Log.e("JackpotApiService", "Error getting jackpots: ${e.message}")
            failure(JackpotApiServiceException("Error getting jackpots: ${e.message}"))
        }
    }

    override suspend fun getLastJackpot(): Result<Jackpot> = withContext(Dispatchers.IO) {
        return@withContext try {
            getJackpots()
                .onSuccess { jackpots ->
                    if (jackpots.isNotEmpty()) {
                        return@withContext success(jackpots.last())
                    }
                }
                .onFailure {
                    val message =  it.message ?: ErrorMessages.UNDEFINED_ERROR
                    throw JackpotApiServiceException("Error getting last jackpot: $message")
                }
            failure(JackpotApiServiceException("Error getting last jackpot"))
        } catch (e: Exception) {
            Log.e("JackpotApiService", "Error getting last jackpot: ${e.message}")
            failure(JackpotApiServiceException("Error getting last jackpot: ${e.message}"))
        }
    }

    override suspend fun postJackpot(jackpot: Jackpot): Result<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = apiService.postJackpotCall(jackpot)
            response.handleExceptions()
                .onSuccess {
                    response.body()?.let {
                        Log.d("JackpotApiService", "Jackpot posted successfully: ${response.body().toString()}")
                        val entityId: String = response.body()?.get("name") ?: throw JackpotApiServiceException("${ErrorCodes.BAD_REQUEST} - ${ErrorMessages.BAD_REQUEST}: Error posting jackpot")

                        return@withContext success(entityId)
                    }
                }
                .onFailure {
                    val message =  response.errorBody()?.string() ?: ErrorMessages.UNDEFINED_ERROR
                    throw JackpotApiServiceException("Error posting jackpot: $message")
                }

            failure(JackpotApiServiceException("Error putting jackpot: ${response.errorBody()?.string()}"))
        } catch (e: Exception) {
            Log.e("JackpotApiService", "Error posting jackpot: ${e.message}")
            failure(JackpotApiServiceException("Error posting jackpot: ${e.message}"))
        }
    }

    override suspend fun putJackpot(
        jackpotId: String,
        jackpot: Jackpot
    ): Result<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = apiService.putJackpotCall(
                jackpotId = jackpotId,
                jackpot = jackpot
            )
            response.handleExceptions()
                .onSuccess {
                    response.body()?.let {
                        Log.d("JackpotApiService", "Jackpot put successfully: ${response.body().toString()}")
                        val entityId: String = response.body()?.get("name") ?: throw JackpotApiServiceException("${ErrorCodes.BAD_REQUEST} - ${ErrorMessages.BAD_REQUEST}: Error putting jackpot")

                        return@withContext success(entityId)
                    }
                }
                .onFailure {
                    val message =  response.errorBody()?.string() ?: ErrorMessages.UNDEFINED_ERROR
                    throw JackpotApiServiceException("Error putting jackpot: $message")
                }

            failure(JackpotApiServiceException("Error putting jackpot: ${response.errorBody()?.string()}"))
        } catch (e: Exception) {
            Log.e("JackpotApiService", "Error putting jackpot: ${e.message}")
            failure(JackpotApiServiceException("Error putting jackpot"))
        }
    }

    private fun buildMoshiJackpotTreeMapReader(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(object {
                @ToJson
                fun toJson(timestamp: Timestamp): Map<String, Any> {
                    return mapOf(
                        "seconds" to timestamp.seconds,
                        "nanoseconds" to timestamp.nanoseconds
                    )
                }

                @FromJson
                fun fromJson(map: Map<String, Any>): Timestamp {
                    return Timestamp(map["seconds"] as Long, map["nanoseconds"] as Int)
                }

                @ToJson
                fun toJson(number: Number): String {
                    return number.toString()
                }

                @FromJson
                fun fromJson(string: String): Number {
                    return string.toDoubleOrNull() ?: string.toDoubleOrNull() as Number
                }
            })
            .build()
    }

    private fun buildMoshiJackpotTreeMapAdapter(moshi: Moshi): JsonAdapter<Map<*, *>> {
        return moshi.adapter(Map::class.java)
            .serializeNulls()
            .nonNull()
    }

    private fun <T> Response<T>.handleExceptions(): Result<T> {
        if (isSuccessful) {
            body()?.let {
                return success(it)
            }
        }

        return failure(JackpotApiServiceException("Error getting data from server: Code: ${code()}, Body: ${errorBody()?.string()}"))
    }
}

class JackpotApiServiceException(message: String): Exception(message)

object ErrorCodes {
    const val UNDEFINED_ERROR = 500
    const val BAD_REQUEST = 400
}

object ErrorMessages {
    const val UNDEFINED_ERROR = "Undefined Error"
    const val BAD_REQUEST = "Bad Request"
}
