package com.serrriy.aviascan.data.flights

import kotlinx.serialization.Serializable

@Serializable
data class CreateFlightRequest(
    val userId: String,
    val flightCode: String,
    val flightDate: String,
    val departureAirport: String,
    val arrivalAirport: String,
    val flightDistance: Float,
    val flightTime: Float
)
