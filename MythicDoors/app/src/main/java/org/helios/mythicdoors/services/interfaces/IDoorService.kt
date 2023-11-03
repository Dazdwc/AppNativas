package org.helios.mythicdoors.services.interfaces

import org.helios.mythicdoors.model.entities.User

interface IDoorService {
    suspend fun geUser(id: Long): User?
    suspend fun getUsers(): List<User>?
    suspend fun saveUser(user: User): Boolean
    suspend fun deleteUser(user: User): Boolean
    suspend fun countUsers(): Int
    suspend fun getLastUser(): User?

}