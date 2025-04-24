package com.serrriy.aviascan.feed.data

import com.serrriy.aviascan.data.feed.FeedListItemDto
import com.serrriy.aviascan.data.flights.FlightListItemDto
import com.serrriy.aviascan.data.posts.PostDto
import com.serrriy.aviascan.data.user.UserResponse

data class FeedListItem(
    val profile: UserResponse,
    val post: PostDto,
    val flight: FlightListItemDto,
)

fun FeedListItemDto.toFeedListItem() = FeedListItem(
    profile = user,
    post = post,
    flight = flight,
)