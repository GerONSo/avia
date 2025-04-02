package com.serrriy.aviascan.feed.data

import com.serrriy.aviascan.data.feed.FeedListItemDto
import com.serrriy.aviascan.data.feed.FeedListItemProfileDto
import com.serrriy.aviascan.data.flights.FlightListItemDto

data class FeedListItem(
    val profile: FeedListItemProfileDto,
    val mapSnapshot: String,
    val title: String,
    val flight: FlightListItemDto,
)

fun FeedListItemDto.toFeedListItem() = FeedListItem(
    profile = profile,
    mapSnapshot = mapSnapshot,
    title = title,
    flight = flight,
)