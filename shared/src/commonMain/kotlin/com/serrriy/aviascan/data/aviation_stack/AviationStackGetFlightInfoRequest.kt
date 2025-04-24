package com.serrriy.aviascan.data.aviation_stack

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AviationStackGetFlightInfoRequest(
    @SerialName("access_key") val accessKey: String,
    @SerialName("flight_date") val flightDate: String?,
    @SerialName("flight_iata") val flightNumber: String?,
    @SerialName("dep_iata") val departureCode: String?,
    @SerialName("arr_iata") val arrivalCode: String?,
)
