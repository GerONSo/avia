package com.serrriy.aviascan.data.user

import kotlinx.serialization.Serializable

@Serializable
data class ChangeUserInfoRequest(
    val userId: String,
    val name: String,
)
