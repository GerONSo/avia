package com.serrriy.aviascan.data.user

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String? = null,
)
