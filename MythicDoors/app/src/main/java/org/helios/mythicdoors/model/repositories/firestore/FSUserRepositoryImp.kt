package org.helios.mythicdoors.model.repositories.firestore

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.utils.connection.firestone.Contracts
import org.helios.mythicdoors.utils.connection.firestone.Contracts.UsersDocumentContract
import org.helios.mythicdoors.utils.connection.firestone.FirestoreClient
import org.helios.mythicdoors.utils.connection.firestone.FirestoreCollection
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.Result.Companion.success
import kotlin.Result.Companion.failure

class FSUserRepositoryImp(): IRepository<User> {

    private val usersCollection = FirestoreClient.getInstance().getCollection(FirestoreCollection.USERS)

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val adapter = moshi.adapter(User::class.java)

    override suspend fun getAll(): List<User> = withContext(Dispatchers.IO) {
        val usersList: MutableList<User> = mutableListOf()

        return@withContext try {
            val snapshot: QuerySnapshot = usersCollection
                .get()
                .addOnFailureListener { e ->
                    Log.e("FSUserRepositoryImp", "Error getting all users: ${e.message}")
                }
                .await()
            snapshot.documents.map {
                val user: User = mapUser(it.data ?: throw UsersRepositoryException("Error getting all users"))
                usersList.add(user)
            }
            usersList
        } catch (e: Exception) {
            Log.e("FSUserRepositoryImp", "Error getting all users: ${e.message}")
            emptyList<User>()
        }
    }

    override suspend fun getOne(key: String): Result<User?> = withContext(Dispatchers.IO) {
        return@withContext try {
            val snapshot: QuerySnapshot = usersCollection
                .whereEqualTo(UsersDocumentContract.FIELD_NAME_EMAIL, key)
                .get()
                .addOnFailureListener { e ->
                    Log.d("FSUserRepositoryImp", "Cannot get the user: ${e.message}")
                }
                .await()

            if (snapshot.documents.isEmpty()) return@withContext success(null)

            val user: User = mapUser(snapshot.documents.first().data ?: throw UsersRepositoryException("Error getting user"))
            success(user)
        } catch (e: Exception) {
            Log.e("FSUserRepositoryImp", "Error getting user: ${e.message}")
            failure(e)
        }
    }

    override suspend fun insertOne(item: User): Result<Boolean> = withContext(Dispatchers.IO) {
        var success: Result<Boolean> = success(false)

        return@withContext try {
            usersCollection.document(item.getEmail())
                .set(buildUser(item))
                .addOnSuccessListener { documentReference ->
                    Log.d("FSUserRepositoryImp", "User successfully inserted!: $documentReference")
                    success = success(true)
                }
                .addOnFailureListener { e ->
                    Log.e("FSUserRepositoryImp", "Error inserting user: ${e.message}")
                    success = success(false)
                }
                .await()
            success
        } catch (e: Exception) {
            Log.e("FSUserRepositoryImp", "Error inserting user: ${e.message}")
            failure(e)
        }
    }

    override suspend fun updateOne(item: User): Result<Boolean> = withContext(Dispatchers.IO) {
        var success: Result<Boolean> = success(false)

        return@withContext try {
            usersCollection
                .document(item.getEmail())
                .update(buildUser(item))
                .addOnSuccessListener {
                    Log.d("FSUserRepositoryImp", "User successfully updated!")
                    success = success(true)
                }
                .addOnFailureListener { e ->
                    Log.e("FSUserRepositoryImp", "Error updating user: ${e.message}")
                    success = success(false)
                }
                .await()
            success
        } catch (e: Exception) {
            Log.e("FSUserRepositoryImp", "Error updating user: ${e.message}")
            failure(e)
        }
    }

    override suspend fun deleteOne(key: String): Result<Boolean> = withContext(Dispatchers.IO) {
        var success: Result<Boolean> = success(false)

        return@withContext try {
            usersCollection.document(key)
                .delete()
                .addOnSuccessListener {
                    Log.d("FSUserRepositoryImp", "User successfully deleted!")
                    success = success(true)
                }
                .addOnFailureListener { e ->
                    Log.e("FSUserRepositoryImp", "Error deleting user: ${e.message}")
                    success = success(false)
                }
                .await()
            success
        } catch (e: Exception) {
            Log.e("FSUserRepositoryImp", "Error deleting user: ${e.message}")
            failure(e)
        }
    }

    override suspend fun count(): Int = withContext(Dispatchers.IO) {
        return@withContext try {
            usersCollection.get().await().size()
        } catch (e: Exception) {
            Log.e("FSUserRepositoryImp", "Error counting users: ${e.message}")
            0
        }
    }

    override suspend fun getLast(): Result<User> = withContext(Dispatchers.IO) {
        return@withContext try {
            val snapShot: QuerySnapshot = usersCollection
                .orderBy(UsersDocumentContract.FIELD_NAME_CREATED_AT)
                .get()
                .addOnFailureListener { e ->
                    Log.e("FSUserRepositoryImp", "Error getting last user: ${e.message}")
                }
                .await()

            val user: User = mapUser(snapShot.documents.last().data ?: throw UsersRepositoryException("Error getting last user"))
            success(user)
        } catch (e: Exception) {
            Log.e("FSUserRepositoryImp", "Error getting last user: ${e.message}")
            failure(e)
        }
    }

    private fun buildUser(item: User): Map<String, Any> {
        return adapter.toJson(item).let {
            moshi.adapter<Map<String, Any>>(Map::class.java).fromJson(it) ?: throw UsersRepositoryException("Error building user")
        }
    }

    private fun mapUser(item: MutableMap<String, Any>): User {
        return adapter.fromJson(moshi.adapter<Map<String, Any>>(Map::class.java).toJson(item) ?: throw UsersRepositoryException("Error mapping user"))
            ?: throw UsersRepositoryException("Error mapping user")
    }
}

class UsersRepositoryException(message: String): Exception(message)