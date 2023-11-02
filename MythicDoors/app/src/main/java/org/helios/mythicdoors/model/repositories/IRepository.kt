package org.helios.mythicdoors.model.repositories

interface IRepository<T> {
    suspend fun getAll(): List<T>
    suspend fun getOne(id: Long): T
    suspend fun insertOne(item: T): Long
//    suspend fun insertMany(items: List<T>): List<Long>
    suspend fun updateOne(item: T): Long
//    suspend fun updateMany(items: List<T>): List<Long>
    suspend fun deleteOne(id: Long): Long
//    suspend fun deleteMany(items: List<T>): List<Long>
    suspend fun count(): Int
    suspend fun getLast(): T
}