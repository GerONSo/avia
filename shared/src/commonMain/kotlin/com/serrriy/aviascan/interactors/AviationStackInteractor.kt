package com.serrriy.aviascan.interactors

import com.serrriy.aviascan.aviationStackClient
import com.serrriy.aviascan.data.aviation_stack.AviationStackGetFlightInfoRequest
import com.serrriy.aviascan.data.aviation_stack.AviationStackGetFlightInfoResponse
import com.serrriy.aviascan.utils.safeCall
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol
import io.ktor.http.path

class AviationStackInteractor {

    suspend fun getFlightInfo(request: AviationStackGetFlightInfoRequest): Result<AviationStackGetFlightInfoResponse> {
        return safeCall {
            aviationStackClient.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.aviationstack.com"
                    path("/v1/flights")
                    parameter("access_key", request.accessKey)
                    parameter("limit", 5)
                    request.flightNumber?.let { parameter("flight_iata", request.flightNumber) }
                    request.flightDate?.let { parameter("flight_date", request.flightDate) }
                    request.departureCode?.let { parameter("dep_iata", request.departureCode) }
                    request.arrivalCode?.let { parameter("arr_iata", request.arrivalCode) }
                }
            }
        }
    }
}