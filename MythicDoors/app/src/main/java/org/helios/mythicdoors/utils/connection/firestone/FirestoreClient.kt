package org.helios.mythicdoors.utils.connection.firestone

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreClient private constructor() {
    private val firestoreClient: FirebaseFirestore = Firebase.firestore

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
        return firestoreClient.collection(collectionName.collectionName)
    }
}

enum class FirestoreCollection(
    val collectionName: String
) {
    USERS("users"),
    GAMES("games")
}