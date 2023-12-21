package org.helios.mythicdoors.model.repositories.sqlite

interface IRepository<T> {
    suspend fun getAll(): List<T>
    suspend fun getOne(id: Long): T
    suspend fun insertOne(item: T): Long
    suspend fun updateOne(item: T): Long
    suspend fun deleteOne(id: Long): Long
    suspend fun count(): Int
    suspend fun getLast(): T
}