package com.serrriy.aviascan.data.flights

import kotlinx.serialization.Serializable

@Serializable
data class FlightTargetDto(
    val flightTime: String,
    val timeZone: String,
    val airport: String,
)
