package com.serrriy.aviascan.data.user

import com.serrriy.aviascan.data.user.UserDto
import com.serrriy.aviascan.data.user.TokenResponse
import kotlinx.serialization.Serializable

@Serializable
data class UserWithTokenResponse(
    val token: TokenResponse,
    val userId: String,
    val userName: String,
)
