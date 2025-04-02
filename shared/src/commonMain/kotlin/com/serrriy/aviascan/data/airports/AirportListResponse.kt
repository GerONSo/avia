package com.serrriy.aviascan.data.airports

import com.serrriy.aviascan.data.airports.AirportDto
import kotlinx.serialization.Serializable

@Serializable
data class AirportListResponse(
    val airports: List<AirportDto>,
)
