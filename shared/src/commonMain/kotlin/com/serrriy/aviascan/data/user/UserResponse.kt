package com.serrriy.aviascan.data.user

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String,
    val name: String,
    val email: String? = null,
    val imageUrl: String? = null,
)
