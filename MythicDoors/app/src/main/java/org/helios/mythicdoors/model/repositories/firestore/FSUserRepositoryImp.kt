package org.helios.mythicdoors.model.repositories.firestore

import android.util.Log
import com.google.firebase.firestore.QuerySnapshot
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

    override suspend fun getOne(key: String): Result<User> = withContext(Dispatchers.IO) {
        return@withContext try {
            val snapshot: QuerySnapshot = usersCollection
                .whereEqualTo(UsersDocumentContract.FIELD_NAME_EMAIL, key)
                .get()
                .addOnFailureListener { e ->
                    Log.d("FSUserRepositoryImp", "Cannot get the user: ${e.message}")
                }
                .await()
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
        return hashMapOf(
            UsersDocumentContract.FIELD_NAME_NAME to item.getName(),
            UsersDocumentContract.FIELD_NAME_EMAIL to item.getEmail(),
            UsersDocumentContract.FIELD_NAME_PASSWORD to item.getPassword(),
            UsersDocumentContract.FIELD_NAME_SCORE to item.getScore(),
            UsersDocumentContract.FIELD_NAME_LEVEL to item.getLevel(),
            UsersDocumentContract.FIELD_NAME_EXPERIENCE to item.getExperience(),
            UsersDocumentContract.FIELD_NAME_COINS to item.getCoins(),
            UsersDocumentContract.FIELD_NAME_GOLD_COINS to item.getGoldCoins(),
            UsersDocumentContract.FIELD_NAME_IS_ACTIVE to item.getIsActive(),
            UsersDocumentContract.FIELD_NAME_CREATED_AT to item.getCreatedAt().toString()
        )
    }

    private fun mapUser(item: MutableMap<String, Any>): User {
        val dateTimeFormatter = DateTimeFormatter.ofPattern(Contracts.DateTimeFormatter.DATE_TIME_FORMAT)

        return User.create(
            name =  item[UsersDocumentContract.FIELD_NAME_NAME] as String,
            email =  item[UsersDocumentContract.FIELD_NAME_EMAIL] as String,
            password =  item[UsersDocumentContract.FIELD_NAME_PASSWORD] as String,
        )
            .apply {
                setScore(item[UsersDocumentContract.FIELD_NAME_SCORE] as Long)
                setLevel(item[UsersDocumentContract.FIELD_NAME_LEVEL] as Long)
                setExperience(item[UsersDocumentContract.FIELD_NAME_EXPERIENCE] as Long)
                setCoins(item[UsersDocumentContract.FIELD_NAME_COINS] as Long)
                setGoldCoins(item[UsersDocumentContract.FIELD_NAME_GOLD_COINS] as Long)
                setIsActive(item[ UsersDocumentContract.FIELD_NAME_IS_ACTIVE] as Boolean)
                setCreatedAt(LocalDateTime.parse(item[UsersDocumentContract.FIELD_NAME_CREATED_AT] as String, dateTimeFormatter))
            }
    }
}

class UsersRepositoryException(message: String): Exception(message)