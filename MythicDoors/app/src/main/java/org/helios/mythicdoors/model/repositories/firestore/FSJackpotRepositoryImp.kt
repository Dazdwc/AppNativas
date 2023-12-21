package org.helios.mythicdoors.model.repositories.firestore

import android.util.Log
import com.google.firebase.Timestamp
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.model.entities.Jackpot
import org.helios.mythicdoors.utils.connection.firestone.Contracts.JackpotsDocumentContract
import org.helios.mythicdoors.utils.connection.firestone.FirestoreClient
import org.helios.mythicdoors.utils.connection.firestone.FirestoreCollection
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

class FSJackpotRepositoryImp(): IRepository<Jackpot> {

    private val jackpotsCollection = FirestoreClient.getInstance().getCollection(FirestoreCollection.JACKPOTS)

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val adapter = moshi.adapter(Jackpot::class.java)

    override suspend fun getAll(): List<Jackpot> = withContext(Dispatchers.IO) {
        val jackpotsList: MutableList<Jackpot> = mutableListOf()

        return@withContext try {
            val snapshot = jackpotsCollection
                .get()
                .addOnFailureListener { e ->
                    Log.e("FSJackpotRepositoryImp", "Error getting all jackpots: ${e.message}")
                }
                .await()
            snapshot.documents.map {
                val jackpot: Jackpot = mapJackpot(it.data ?: throw JackpotsRepositoryException("Error getting all jackpots"))
                jackpotsList.add(jackpot)
            }
            jackpotsList
        } catch (e: Exception) {
            emptyList<Jackpot>()
        }
    }

    override suspend fun getOne(key: String): Result<Jackpot> = withContext(Dispatchers.IO) {
        return@withContext try {
            getLast()
        } catch (e: Exception) {
            Log.e("FSJackpotRepositoryImp", "Error getting jackpot: ${e.message}")
            failure(e)
        }
    }

    override suspend fun insertOne(item: Jackpot): Result<Boolean> = withContext(Dispatchers.IO) {
        TODO("Not yet implemented")
    }

    override suspend fun updateOne(item: Jackpot): Result<Boolean> = withContext(Dispatchers.IO) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteOne(key: String): Result<Boolean> = withContext(Dispatchers.IO) {
        TODO("Not yet implemented")
    }

    override suspend fun getLast(): Result<Jackpot> = withContext(Dispatchers.IO) {
        TODO("Not yet implemented")
    }

    override suspend fun count(): Int = withContext(Dispatchers.IO) {
        TODO("Not yet implemented")
    }

    private fun buildJackpot(item: Jackpot): Map<String, Any> {
        return adapter.toJson(item).let {
            moshi.adapter<Map<String, Any>>(Map::class.java).fromJson(it) ?: throw JackpotsRepositoryException("Error building jackpot")
        }
    }

    private fun mapJackpot(item: Map<String, Any>): Jackpot {
        return adapter.fromJson(moshi.adapter<Map<String, Any>>(Map::class.java).toJson(item) ?: throw JackpotsRepositoryException("Error mapping jackpot"))
            ?: throw JackpotsRepositoryException("Error mapping jackpot")
    }
}

class JackpotsRepositoryException(message: String): Exception(message)