package com.serrriy.aviascan.data.flights

import com.serrriy.aviascan.data.airports.AirportDto
import kotlinx.serialization.Serializable

@Serializable
data class FlightListItemDto(
    val id: String,
    val departureAirport: AirportDto,
    val arrivalAirport: AirportDto,
    val flightDepartureTime: String,
    val flightArrivalTime: String,
    val distance: Double,
    val userId: String
)
