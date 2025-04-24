package com.serrriy.aviascan.add_flight

import com.serrriy.aviascan.data.aviation_stack.AviationStackFlightInfoDto

data class SupposedFlight(
    val flightNumber: String,
    val departureCode: String,
    val arrivalCode: String,
    val departureTime: String,
    val unparsed: AviationStackFlightInfoDto,
)
