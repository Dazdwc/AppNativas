package org.helios.mythicdoors.model.repositories.firestore

interface IRepository<T> {
    suspend fun getAll(): List<T>
    suspend fun getOne(email: String): Result<T>
    suspend fun insertOne(item: T): Result<Boolean>
    suspend fun updateOne(item: T): Result<Boolean>
    suspend fun deleteOne(email: String): Result<Boolean>
    suspend fun count(): Int
    suspend fun getLast(): T
}