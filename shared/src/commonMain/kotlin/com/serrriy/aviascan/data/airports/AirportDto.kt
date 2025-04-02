package com.serrriy.aviascan.data.airports

import kotlinx.serialization.Serializable

@Serializable
data class AirportDto(
    val name: String?,
    val code: String,
    val city: String,
    val country: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
)
