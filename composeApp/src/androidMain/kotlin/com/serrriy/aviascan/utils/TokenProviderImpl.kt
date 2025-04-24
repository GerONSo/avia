package com.serrriy.aviascan.utils

import com.serrriy.aviascan.repositories.DataStoreKeys
import com.serrriy.aviascan.repositories.DataStoreKeys.accessToken
import com.serrriy.aviascan.repositories.DataStoreKeys.refreshToken
import com.serrriy.aviascan.repositories.DataStoreRepository

class TokenProviderImpl(private val dataStoreRepository: DataStoreRepository) : TokenProvider {
    override suspend fun getAccessToken(): String {
        return dataStoreRepository.readFromDataStore(accessToken) ?: ""
    }

    override suspend fun getRefreshToken(): String {
        return dataStoreRepository.readFromDataStore(refreshToken) ?: ""
    }

    override suspend fun setAccessToken(accessToken: String) {
        dataStoreRepository.writeToDataStore(DataStoreKeys.accessToken, accessToken)
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        dataStoreRepository.writeToDataStore(DataStoreKeys.refreshToken, refreshToken)
    }
}