package com.serrriy.aviascan.data.user

import kotlinx.serialization.Serializable

@Serializable
data class GetUserRequest(
    val email: String,
    val password: String,
)
