package com.serrriy.aviascan.interactors

import com.serrriy.aviascan.client
import com.serrriy.aviascan.data.aviation_stack.AviationStackGetFlightInfoRequest
import com.serrriy.aviascan.data.aviation_stack.AviationStackGetFlightInfoResponse
import com.serrriy.aviascan.utils.NetworkError
import com.serrriy.aviascan.utils.Result
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException

class AviationStackInteractor {

    suspend fun getFlightInfo(request: AviationStackGetFlightInfoRequest): Result<AviationStackGetFlightInfoResponse, NetworkError> {
        val a = try {
            client.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.aviationstack.com"
                    path("/v1/flights")
                    parameter("access_key", request.accessKey)
                    parameter("flight_iata", request.flightNumber)
                    parameter("flight_date", request.flightDate)
                }
            }
        } catch(e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch(e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }
        return Result.Success(a.body())
    }
}