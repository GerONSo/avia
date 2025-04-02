package com.serrriy.aviascan.data.user

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val name: String,
    val firebaseId: String
)
