package com.serrriy.aviascan.repositories

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.serrriy.aviascan.utils.dataStore
import kotlinx.coroutines.flow.first

class DataStoreRepository(private val context: Context) {

    suspend fun readFromDataStore(key: Preferences.Key<String>): String? {
        val preferences = context.dataStore.data.first()
        return preferences[key]
    }

    suspend fun writeToDataStore(key: Preferences.Key<String>, value: String) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}

object DataStoreKeys {
    val accessToken = stringPreferencesKey("access_token")
    val refreshToken = stringPreferencesKey("refresh_token")
    val userId = stringPreferencesKey("userId")
}