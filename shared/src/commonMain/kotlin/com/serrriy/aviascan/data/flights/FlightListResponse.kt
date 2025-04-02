package com.serrriy.aviascan.data.flights

import com.serrriy.aviascan.data.flights.FlightListItemDto
import kotlinx.serialization.Serializable

@Serializable
data class FlightListResponse(
    val flights: List<FlightListItemDto>,
)
