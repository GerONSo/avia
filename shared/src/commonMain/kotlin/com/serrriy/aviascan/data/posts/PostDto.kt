package com.serrriy.aviascan.data.posts

import kotlinx.serialization.Serializable

@Serializable
data class PostDto(
    val id: String,
    val userId: String,
    val flightId: String,
    val imageUrl: String?,
    val createdAt: String,
    val isPublished: Boolean,
    val title: String?,
)