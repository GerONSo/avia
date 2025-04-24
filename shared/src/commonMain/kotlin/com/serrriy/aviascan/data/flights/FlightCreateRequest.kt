package com.serrriy.aviascan.data.flights

import kotlinx.serialization.Serializable
import com.serrriy.aviascan.data.flights.FlightTargetDto

@Serializable
data class CreateFlightRequest(
    val userId: String,
    val flightCode: String,
    val departure: FlightTargetDto,
    val arrival: FlightTargetDto,
)
