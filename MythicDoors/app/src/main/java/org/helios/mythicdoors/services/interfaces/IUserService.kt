package org.helios.mythicdoors.services.interfaces

import org.helios.mythicdoors.model.entities.User

interface IUserService {
    suspend fun getUsers(): List<User>?
    suspend fun getUser(id: Long): User?
    suspend fun saveUser(user: User): Boolean
    suspend fun deleteUser(id: Long): Boolean
    suspend fun countUsers(): Int
    suspend fun getLastUser(): User?
}