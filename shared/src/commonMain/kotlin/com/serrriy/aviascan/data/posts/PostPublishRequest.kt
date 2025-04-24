package com.serrriy.aviascan.data.posts

import kotlinx.serialization.Serializable

@Serializable
data class PostPublishRequest(
    val title: String,
    val id: String,
)