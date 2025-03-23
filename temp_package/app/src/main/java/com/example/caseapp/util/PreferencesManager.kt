package com.example.caseapp.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class PreferencesManager private constructor(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFERENCES_FILE,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveAuthToken(token: String) {
        sharedPreferences.edit {
            putString(KEY_AUTH_TOKEN, token)
        }
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    }

    fun saveCurrentUserId(userId: Int) {
        sharedPreferences.edit {
            putInt(KEY_USER_ID, userId)
        }
    }

    fun getCurrentUserId(): Int {
        return sharedPreferences.getInt(KEY_USER_ID, 0)
    }

    fun saveUsername(username: String) {
        sharedPreferences.edit {
            putString(KEY_USERNAME, username)
        }
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(KEY_USERNAME, null)
    }

    fun saveLastSyncTimestamp(timestamp: Long) {
        sharedPreferences.edit {
            putLong(KEY_LAST_SYNC, timestamp)
        }
    }

    fun getLastSyncTimestamp(): Long {
        return sharedPreferences.getLong(KEY_LAST_SYNC, 0)
    }

    fun clearUserData() {
        sharedPreferences.edit {
            remove(KEY_AUTH_TOKEN)
            remove(KEY_USER_ID)
            remove(KEY_USERNAME)
            // No eliminamos el timestamp de sincronización ya que podría ser útil mantenerlo
        }
    }

    companion object {
        private const val PREFERENCES_FILE = "case_app_prefs"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_LAST_SYNC = "last_sync"

        @Volatile
        private var instance: PreferencesManager? = null

        fun getInstance(context: Context): PreferencesManager {
            return instance ?: synchronized(this) {
                instance ?: PreferencesManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }
}