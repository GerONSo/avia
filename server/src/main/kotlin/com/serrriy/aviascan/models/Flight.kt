package com.serrriy.aviascan.models

data class FlightDto(
    val id: String,
    val airportFrom: String,
    val airportTo: String,
    val date: String,
    val flightCode: String,
    val userId: String,
    val flightDistance: Float,
    val flightTime: Float
)
