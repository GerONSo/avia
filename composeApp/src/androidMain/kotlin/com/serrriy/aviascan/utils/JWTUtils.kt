package com.serrriy.aviascan.utils

import com.auth0.android.jwt.JWT
import com.serrriy.aviascan.repositories.DataStoreKeys
import com.serrriy.aviascan.repositories.DataStoreRepository
import java.util.Date

class JWTUtils(
    private val dataStoreRepository: DataStoreRepository
) {
    companion object {
        @Volatile private var instance: JWTUtils? = null

        fun getInstance(dataStoreRepository: DataStoreRepository): JWTUtils {
            return instance ?: synchronized(this) {
                instance ?: JWTUtils(dataStoreRepository).also { instance = it }
            }
        }
    }

    suspend fun getCurrentUserId(): String? {
        val accessToken = dataStoreRepository.readFromDataStore(DataStoreKeys.accessToken)
        accessToken ?: return null
        val decodedJWT = JWT(accessToken)
        return decodedJWT.getClaim("userId").asString()
    }

    suspend fun isAccessTokenValid(): Boolean {
        val accessToken = dataStoreRepository.readFromDataStore(DataStoreKeys.accessToken)
        if (accessToken.isNullOrEmpty()) return false
        val decodedJWT = JWT(accessToken)
        return Date().before(decodedJWT.expiresAt)
    }

    suspend fun isRefreshTokenValid(): Boolean {
        val refreshToken = dataStoreRepository.readFromDataStore(DataStoreKeys.refreshToken)
        if (refreshToken.isNullOrEmpty()) return false
        val decodedJWT = JWT(refreshToken)
        return Date().before(decodedJWT.expiresAt)
    }
}