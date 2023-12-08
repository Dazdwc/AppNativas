package org.helios.mythicdoors.presentation.sign_in

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class FirebaseBaseAuthClient(
    private val auth: FirebaseAuth
): IFirebaseBaseAuthClient {
    companion object {
        @Volatile
        private var instance: FirebaseBaseAuthClient? = null

        fun getInstance(auth: FirebaseAuth): FirebaseBaseAuthClient {
            return instance ?: synchronized(this) {
                instance ?: FirebaseBaseAuthClient(auth).also { instance = it }
            }
        }
    }
    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String,
        name: String?
    ): SignInResult {
        return try {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user

            // TODO: Create a node and save user name

            user?.run {
                SignInResult.success(
                    UserData.create(
                        id = uid,
                        name = name,
                        email = email,
                        photoUrl = photoUrl?.toString()
                    )
                )
            } ?: SignInResult.error("Error registering in with Firebase")
        } catch (e: Exception) {
            if (e is CancellationException) throw FirebaseBaseAuthException(e) else Log.e("FirebaseBaseAuthClient", "Error signing in with Firebase: $e")
            SignInResult.error("Error signing in with Firebase: ${e.message}")
        }
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): SignInResult {
        return try {
            val user = auth.signInWithEmailAndPassword(email, password).await().user

            // TODO: Search for the user node and capture the user name and photoUrl

            user?.run {
                SignInResult.success(
                    UserData.create(
                        id = uid,
                        name = displayName,
                        email = email,
                        photoUrl = photoUrl?.toString()
                    )
                )
            } ?: SignInResult.error("Error signing in with Firebase")
        } catch (e: Exception) {
            if (e is CancellationException) throw FirebaseBaseAuthException(e) else Log.e(
                "FirebaseBaseAuthClient",
                "Error signing in with Firebase: $e"
            )
            SignInResult.error("Error signing in with Firebase: ${e.message}")
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}

class FirebaseBaseAuthException(cause: Throwable): Exception(cause)