package com.serrriy.aviascan.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.serrriy.aviascan.repositories.DataStoreKeys.userId
import com.serrriy.aviascan.repositories.DataStoreRepository
import java.time.OffsetDateTime

fun String.toDateTime(): OffsetDateTime {
    return OffsetDateTime.parse(this)
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

suspend fun withUserIdFrom(
    dataStoreRepository: DataStoreRepository,
    completion: suspend (userId: String) -> Unit
) {
    dataStoreRepository.readFromDataStore(userId)?.let { userId ->
        completion.invoke(userId)
    }
}