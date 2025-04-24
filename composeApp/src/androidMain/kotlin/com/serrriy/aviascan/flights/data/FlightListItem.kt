package com.serrriy.aviascan.flights.data

import com.serrriy.aviascan.data.airports.AirportDto
import com.serrriy.aviascan.data.flights.FlightListItemDto

data class FlightListItem(
    val id: String,
    val departure: FlightTargetDto,
    val arrival: FlightTargetDto,
    var expanded: Boolean,
    val distance: String,
)

data class FlightTargetDto(
    val airport: AirportDto,
    val datetime: String,
)

fun FlightListItemDto.toFlightListItem() = FlightListItem(
    id = id,
    departure = FlightTargetDto(
        airport = departureAirport,
        datetime = flightDepartureTime,
    ),
    arrival = FlightTargetDto(
        airport = arrivalAirport,
        datetime = flightArrivalTime,
    ),
    expanded = false,
    distance = "$distance",
)