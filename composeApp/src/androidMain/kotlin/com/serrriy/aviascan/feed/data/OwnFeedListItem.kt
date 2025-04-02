package com.serrriy.aviascan.feed.data

import com.serrriy.aviascan.data.feed.FeedListItemDto
import com.serrriy.aviascan.data.feed.FeedListItemProfileDto
import com.serrriy.aviascan.data.flights.FlightListItemDto

data class OwnFeedListItem(
    val profile: FeedListItemProfileDto,
    val mapSnapshot: String,
    val title: String,
    val flight: FlightListItemDto,
)

fun FeedListItemDto.toOwnFeedListItem() = OwnFeedListItem(
    profile = profile,
    mapSnapshot = mapSnapshot,
    title = title,
    flight = flight,
)