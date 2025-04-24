package com.serrriy.aviascan.interactors

import com.serrriy.aviascan.data.achievements.AchievementListResponse
import com.serrriy.aviascan.getClient
import com.serrriy.aviascan.utils.TokenProvider
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.path

class AchievementInteractor(
    private val tokenProvider: TokenProvider,
) {
    suspend fun getAllAchievements(userId: String): AchievementListResponse {
        return getClient(tokenProvider).get {
            url {
                path("/achievements/list/$userId")
            }
            header("Authorization", "Bearer ${tokenProvider.getAccessToken()}")
        }.body()
    }
}