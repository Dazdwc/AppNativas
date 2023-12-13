package org.helios.mythicdoors.services.firestore

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.model.repositories.firestore.FSUserRepositoryImp
import kotlin.Result.Companion.success

class FSUserServiceImp(
    private val userRepository: FSUserRepositoryImp = FSUserRepositoryImp()
) : IDataService<User> {
    override suspend fun getAll(): List<User>? = withContext(Dispatchers.IO){
        return@withContext try {
            userRepository.getAll().takeIf { it.isNotEmpty() }
        } catch (e: Exception) {
            Log.e("FSUserServiceImp", "Error getting all users: ${e.message}")
            null
        }
    }

    override suspend fun getOne(email: String): User? = withContext(Dispatchers.IO) {
        return@withContext  try {
            userRepository.getOne(key = email).getOrNull()
        } catch (e: Exception) {
            Log.e("FSUserServiceImp", "Error getting user: ${e.message}")
            null
        }
    }

    override suspend fun saveOne(item: User): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            item.takeIf { it.isValid() }?.run {
                if (isFirestoreEmpty()) checkIfUserExists(this).takeIf { !it }?.let { userRepository.insertOne(this) } ?: success(false)
                else userRepository.updateOne(this)
            } ?: success(false)
        } catch (e: Exception) {
            Log.e("FSUserServiceImp", "Error inserting user: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun deleteOne(item: User): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            if (checkIfUserExists(item)) userRepository.deleteOne(item.getEmail())
            else success(false)
        } catch (e: Exception) {
            Log.e("FSUserServiceImp", "Error deleting user: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun count(): Int = withContext(Dispatchers.IO) {
        return@withContext try {
            userRepository.count()
        } catch (e: Exception) {
            Log.e("FSUserServiceImp", "Error counting users: ${e.message}")
            0
        }
    }

    override suspend fun getLast(): User? = withContext(Dispatchers.IO) {
        return@withContext try {
            userRepository.getLast().getOrNull()
        } catch (e: Exception) {
            Log.e("FSUserServiceImp", "Error getting last user: ${e.message}")
            null
        }
    }

    private suspend fun checkIfUserExists(user: User): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            userRepository.getOne(user.getEmail()).getOrNull() != null
        } catch (e: Exception) {
            Log.e("FSUserServiceImp", "Error checking if user exists: ${e.message}")
            false
        }
    }
}