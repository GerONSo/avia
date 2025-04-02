package com.serrriy.aviascan.flights.data

import com.serrriy.aviascan.data.airports.AirportDto
import com.serrriy.aviascan.data.flights.FlightListItemDto

data class FlightListItem(
    val id: String,
    val departure: FlightTargetDto,
    val arrival: FlightTargetDto,
    var expanded: Boolean,
)

data class FlightTargetDto(
    val airport: AirportDto,
    val datetime: String,
)

fun FlightListItemDto.toFlightListItem() = FlightListItem(
    id = id,
    departure = FlightTargetDto(
        airport = departureAirport,
        datetime = "2025-01-12T13:40",
    ),
    arrival = FlightTargetDto(
        airport = arrivalAirport,
        datetime = "2025-01-12T13:40",
    ),
    expanded = false,
)