package com.serrriy.aviascan.data.feed

import com.serrriy.aviascan.data.flights.FlightListItemDto
import com.serrriy.aviascan.data.posts.PostDto
import com.serrriy.aviascan.data.user.UserResponse
import kotlinx.serialization.Serializable

@Serializable
data class FeedListItemDto(
    val user: UserResponse,
    val post: PostDto,
    val flight: FlightListItemDto,
)

@Serializable
data class FeedListResponse(
    val posts: List<FeedListItemDto>
)
