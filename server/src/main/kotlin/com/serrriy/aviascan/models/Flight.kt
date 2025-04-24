package com.serrriy.aviascan.models
import java.time.OffsetDateTime

data class FlightDto(
    val id: String,
    val airportFrom: String,
    val airportTo: String,
    val flightDepartureTime: OffsetDateTime,
    val flightArrivalTime: OffsetDateTime,
    val flightCode: String,
    val userId: String,
)
