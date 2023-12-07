package org.helios.mythicdoors.utils.connection.firestone

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreClient private constructor() {
    private val firestone: FirebaseFirestore = FirebaseFirestore.getInstance()

    companion object {
        @Volatile
        private var instance: FirestoreClient? = null

        fun getInstance(): FirestoreClient {
            return instance ?: synchronized(this) {
                instance ?: FirestoreClient().also { instance = it }
            }
        }
    }

    fun getCollection(collectionName: FirestoreCollection): CollectionReference {
        return getInstance().firestone.collection(collectionName.collectionName)
    }
}

enum class FirestoreCollection(
    val collectionName: String
) {
    USERS("users"),
    GAMES("games")
}