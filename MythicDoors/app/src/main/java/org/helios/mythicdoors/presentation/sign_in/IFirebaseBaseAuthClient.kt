package org.helios.mythicdoors.presentation.sign_in

interface IFirebaseBaseAuthClient {
    suspend fun registerWithEmailAndPassword(email: String, password: String, name: String?): SignInResult
    suspend fun signInWithEmailAndPassword(email: String, password: String): SignInResult
    suspend fun signOut()
}