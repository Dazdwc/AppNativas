package org.helios.mythicdoors.presentation.sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import org.helios.mythicdoors.R
import java.util.concurrent.CancellationException

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth

    suspend fun signIn(): IntentSender? {
        return try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()?.pendingIntent?.intentSender
        } catch (e: Exception) {
            if (e is CancellationException) throw GoogleAuthUiException(e) else Log.e("GoogleAuthUiClient", "Error signing in with Google: $e")
            null
        }
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential: SignInCredential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken: String? = credential.googleIdToken
        val googleCredentials: AuthCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user

            user?.run {
                SignInResult.success(
                    UserData.create(
                        id = uid,
                        name = displayName,
                        email = email,
                        photoUrl = photoUrl?.toString()
                    )
                )
            } ?: SignInResult.error("Error signing in with Google")
        } catch (e: Exception) {
            if (e is CancellationException) throw GoogleAuthUiException(e) else Log.e("GoogleAuthUiClient", "Error signing in with Google: $e")
            SignInResult.error("Error signing in with Google: ${e.message}")
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            if (e is CancellationException) throw GoogleAuthUiException(e) else Log.e("GoogleAuthUiClient", "Error signing out with Google: $e")
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData.create(
            id = uid,
            name = displayName,
            email = email,
            photoUrl = photoUrl?.toString()
        )
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                buildGoogleIdTokenRequest()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    private fun buildGoogleIdTokenRequest(): GoogleIdTokenRequestOptions {
        return GoogleIdTokenRequestOptions.Builder()
            .setSupported(true)
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.google_webclient_auth_id))
            .build()
    }
}

class GoogleAuthUiException(cause: Throwable) : Exception(cause)