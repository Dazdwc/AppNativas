package org.helios.mythicdoors.utils.encryption

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.helios.mythicdoors.R

class KryptoClient(
    context: Context
) {

    private val masterKey: MasterKey = MasterKey.Builder(
        context,
        MasterKey.DEFAULT_MASTER_KEY_ALIAS
    )
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secret_shared_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    companion object {
        @Volatile
        private var instance: KryptoClient? = null

        fun getInstance(context: Context): KryptoClient {
            return instance ?: synchronized(this) {
                instance ?: buildKryptoClient(context).also { instance = it }
            }
        }

        private fun buildKryptoClient(context: Context): KryptoClient {
            return KryptoClient(context)
        }
    }

    fun encryptData(data: String, context: Context): String {
        return sharedPreferences.edit().putString(
            context.getString(R.string.key_for_password_encryption),
            data
        ).apply().toString()
    }

    fun decryptData(context: Context): String {
        return sharedPreferences.getString(context.getString(
            R.string.key_for_password_encryption),
            null
        ).toString()
    }
}