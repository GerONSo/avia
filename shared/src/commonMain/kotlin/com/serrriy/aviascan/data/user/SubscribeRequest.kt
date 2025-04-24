package com.serrriy.aviascan.data.user

import kotlinx.serialization.Serializable

@Serializable
data class SubscribeRequest(
    val userId: String,
    val subscribeTo: String,
)
