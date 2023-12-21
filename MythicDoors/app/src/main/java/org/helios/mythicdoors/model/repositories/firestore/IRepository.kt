package org.helios.mythicdoors.model.repositories.firestore

interface IRepository<T> {
    suspend fun getAll(): List<T>
    suspend fun getOne(key: String): Result<T?>
    suspend fun insertOne(item: T): Result<Boolean>
    suspend fun updateOne(item: T): Result<Boolean>
    suspend fun deleteOne(key: String): Result<Boolean>
    suspend fun count(): Int
    suspend fun getLast(): Result<T>
}