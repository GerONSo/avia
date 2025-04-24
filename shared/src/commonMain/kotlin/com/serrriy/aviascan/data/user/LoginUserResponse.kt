package com.serrriy.aviascan.data.user

import kotlinx.serialization.Serializable

@Serializable
data class LoginUserResponse(
    val accessToken: String,
    val refreshToken: String,
)