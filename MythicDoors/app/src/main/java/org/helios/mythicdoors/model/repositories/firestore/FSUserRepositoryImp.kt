package org.helios.mythicdoors.model.repositories.firestore

import android.util.Log
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.utils.connection.firestone.Contracts.UsersDocumentContract
import org.helios.mythicdoors.utils.connection.firestone.FirestoreClient
import org.helios.mythicdoors.utils.connection.firestone.FirestoreCollection
import java.time.LocalDate
import kotlin.Result.Companion.success

class FSUserRepositoryImp(): IRepository<User> {
    private val firestoneClient: FirestoreClient = FirestoreClient.getInstance()
    private val usersCollection by lazy { firestoneClient.getCollection(FirestoreCollection.USERS) }

    override suspend fun getAll(): List<User> = withContext(Dispatchers.IO) {
        val usersList: MutableList<User> = mutableListOf()

        return@withContext try {
            val snapshot: QuerySnapshot = usersCollection.get().await()
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

    override suspend fun getOne(key: String): Result<User> = withContext(Dispatchers.IO) {
        return@withContext try {
            val snapshot: QuerySnapshot = usersCollection.whereEqualTo(UsersDocumentContract.FIELD_NAME_EMAIL, key).get().await()
            val user: User = mapUser(snapshot.documents.first().data ?: throw UsersRepositoryException("Error getting user"))
            success(user)
        } catch (e: Exception) {
            Log.e("FSUserRepositoryImp", "Error getting user: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun insertOne(item: User): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            usersCollection.document(item.getEmail())
                .set(buildUser(item))
                .await()
            success(true)
        } catch (e: Exception) {
            Log.e("FSUserRepositoryImp", "Error inserting user: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun updateOne(item: User): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            usersCollection.document(item.getEmail())
                .update(buildUser(item))
                .await()
            success(true)
        } catch (e: Exception) {
            Log.e("FSUserRepositoryImp", "Error updating user: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun deleteOne(key: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            usersCollection.document(key)
                .delete()
                .await()
            success(true)
        } catch (e: Exception) {
            Log.e("FSUserRepositoryImp", "Error deleting user: ${e.message}")
            Result.failure(e)
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

    override suspend fun getLast(): User = withContext(Dispatchers.IO) {
        return@withContext try {
            val snapShot: QuerySnapshot = usersCollection.orderBy(UsersDocumentContract.FIELD_NAME_CREATED_AT).get().await()
            mapUser(snapShot.documents.last().data ?: throw UsersRepositoryException("Error getting last user"))
        } catch (e: Exception) {
            Log.e("FSUserRepositoryImp", "Error getting last user: ${e.message}")
            User.createEmptyUser()
        }
    }

    private fun buildUser(item: User): Map<String, String> {
        return hashMapOf(
            UsersDocumentContract.FIELD_NAME_NAME to item.getName(),
            UsersDocumentContract.FIELD_NAME_EMAIL to item.getEmail(),
            UsersDocumentContract.FIELD_NAME_PASSWORD to item.getPassword(),
            UsersDocumentContract.FIELD_NAME_SCORE to item.getScore().toString(),
            UsersDocumentContract.FIELD_NAME_LEVEL to item.getLevel().toString(),
            UsersDocumentContract.FIELD_NAME_EXPERIENCE to item.getExperience().toString(),
            UsersDocumentContract.FIELD_NAME_COINS to item.getCoins().toString(),
            UsersDocumentContract.FIELD_NAME_GOLD_COINS to item.getGoldCoins().toString(),
            UsersDocumentContract.FIELD_NAME_IS_ACTIVE to item.getIsActive().toString(),
            UsersDocumentContract.FIELD_NAME_CREATED_AT to item.getCreatedAt().toString()
        )
    }

    private fun mapUser(item: MutableMap<String, Any>): User {
        return User.create(
            documentId = item[UsersDocumentContract.FIELD_NAME_ID] as String?,
            name =  item[UsersDocumentContract.FIELD_NAME_NAME] as String,
            email =  item[UsersDocumentContract.FIELD_NAME_EMAIL] as String,
            password =  item[UsersDocumentContract.FIELD_NAME_PASSWORD] as String,
        )
            .apply {
                setScore(item[UsersDocumentContract.FIELD_NAME_SCORE] as Int)
                setLevel(item[UsersDocumentContract.FIELD_NAME_LEVEL] as Int)
                setExperience(item[UsersDocumentContract.FIELD_NAME_EXPERIENCE] as Int)
                setCoins(item[UsersDocumentContract.FIELD_NAME_COINS] as Int)
                setGoldCoins(item[UsersDocumentContract.FIELD_NAME_GOLD_COINS] as Int)
                setIsActive(item[ UsersDocumentContract.FIELD_NAME_IS_ACTIVE] as Boolean)
                setCreatedAt(item[UsersDocumentContract.FIELD_NAME_CREATED_AT] as LocalDate)
            }
    }
}

class UsersRepositoryException(message: String): Exception(message)