package com.serrriy.aviascan.data.aviation_stack

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AviationStackGetFlightInfoResponse(
    @SerialName("data") val data: List<AviationStackFlightInfoDto>?,
    @SerialName("error") val error: AviationStackError?
)

@Serializable
data class AviationStackFlightInfoDto(
    @SerialName("flight_date") val flightDate: String,
    @SerialName("departure") val departure: AviationStackAirportDataDto,
    @SerialName("arrival") val arrival: AviationStackAirportDataDto,
    @SerialName("flight") val flight: AviationStackFlightDataDto,
)

@Serializable
data class AviationStackFlightDataDto(
    @SerialName("iata") val iata: String,
)

@Serializable
data class AviationStackAirportDataDto(
    @SerialName("airport") val airport: String,
    @SerialName("iata") val iata: String,
    @SerialName("scheduled") val scheduled: String,
    @SerialName("timezone") val timezone: String,
)

@Serializable
data class AviationStackError(
    @SerialName("code") val code: String,
)