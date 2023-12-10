package org.helios.mythicdoors.services.firestore

/* We could migrate the interfaces into one common interface */
interface IDataService<T> {
    suspend fun getAll(): List<T>?
    suspend fun getOne(email: String): T?
    suspend fun saveOne(item: T): Result<Boolean>
    suspend fun deleteOne(item: T): Result<Boolean>
    suspend fun count(): Int
    suspend fun getLast(): T?
}