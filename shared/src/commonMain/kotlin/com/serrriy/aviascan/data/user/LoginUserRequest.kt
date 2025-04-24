package com.serrriy.aviascan.data.user

import kotlinx.serialization.Serializable

@Serializable
data class LoginUserRequest(
    val email: String,
    val password: String,
)
