package org.helios.mythicdoors.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.model.repositories.IRepository
import org.helios.mythicdoors.model.repositories.UserRepositoryImp
import org.helios.mythicdoors.services.interfaces.IUserService
import org.helios.mythicdoors.utils.Connection

class UserServiceImp(dbHelper: Connection): IUserService {
    private var repository: IRepository<User>

    init { repository = UserRepositoryImp(dbHelper) }

    override suspend fun getUsers(): List<User>? = withContext(Dispatchers.IO) {
        try {
            return@withContext repository.getAll().takeIf { it.isNotEmpty() }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    override suspend fun getUser(id: Long): User? = withContext(Dispatchers.IO) {
        try {
            return@withContext repository.getOne(id).takeIf { !it.isEmpty() }
        } catch(e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    override suspend fun getLastUser(): User? = withContext(Dispatchers.IO) {
        try {
            return@withContext repository.getLast().takeIf { !it.isEmpty() }
//            if(repository.getAll().any()) return@withContext repository.getLast() else null
        } catch(e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    /* El método save() decide por sí mismo si tiene que realizar una operación insert o una update. */
    override suspend fun saveUser(user: User): Boolean = withContext(Dispatchers.IO) {
        try {
            user.takeIf { it.isValid() }?.run {
                return@withContext if (isEmpty()) checkIfUserExists(this).takeIf { !it }?.let { repository.insertOne(this) > 0 } ?: false
                else repository.updateOne(this) > 0
            } ?: return@withContext false
        } catch(e: Exception) {
            e.printStackTrace()
        }
        return@withContext false
    }

    override suspend fun deleteUser(id: Long): Boolean = withContext(Dispatchers.IO) {
        try {
            if (id > 0) return@withContext getUser(id).takeIf { it != null }?.let { repository.deleteOne(id) > 0 } ?: false
        } catch(e: Exception) {
            e.printStackTrace()
        }
        return@withContext false
    }

    override suspend fun countUsers(): Int = withContext(Dispatchers.IO) {
        try {
            return@withContext getUsers()?.size ?: 0
        } catch(e: Exception) {
            e.printStackTrace()
            return@withContext 0
        }
    }

    private suspend fun checkIfUserExists(user: User): Boolean = withContext(Dispatchers.IO) {
        try {
            return@withContext repository.getAll().any { it.getEmail() == user.getEmail() }
        } catch(e: Exception) {
            e.printStackTrace()
        }
        return@withContext false
    }
}