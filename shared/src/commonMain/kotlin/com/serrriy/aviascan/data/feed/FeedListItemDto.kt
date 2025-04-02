package com.serrriy.aviascan.data.feed

import com.serrriy.aviascan.data.flights.FlightListItemDto

data class FeedListItemDto(
    val profile: FeedListItemProfileDto,
    val mapSnapshot: String,
    val title: String,
    val flight: FlightListItemDto,
)

data class FeedListItemProfileDto(
    val avatar: String,
    val name: String,
    val datetime: String,
)
