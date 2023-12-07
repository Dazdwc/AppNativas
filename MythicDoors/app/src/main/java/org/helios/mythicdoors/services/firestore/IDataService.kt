package org.helios.mythicdoors.services.firestore

/* We could migrate the interfaces into one common interface */
interface IDataService<T> {
    suspend fun getAll(): List<T>
    suspend fun getOne(email: String): T
    suspend fun insertOne(item: T): Boolean
    suspend fun updateOne(item: T): Boolean
    suspend fun deleteOne(item: T): Boolean
    suspend fun count(): Int
    suspend fun getLast(): T
}