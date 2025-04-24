package com.serrriy.aviascan.interactors

import com.serrriy.aviascan.data.airports.AirportListResponse
import com.serrriy.aviascan.data.flights.CreateFlightRequest
import com.serrriy.aviascan.data.flights.CreateFlightResponse
import com.serrriy.aviascan.data.flights.FlightListResponse
import com.serrriy.aviascan.getClient
import com.serrriy.aviascan.utils.TokenProvider
import com.serrriy.aviascan.utils.safeCall
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.path
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class FlightsInteractor(
    private val tokenProvider: TokenProvider
) {
    suspend fun getAllFlights(userId: String): FlightListResponse {
        return getClient(tokenProvider).get {
            url {
                path("/flights/list/$userId")
            }
        }.body()
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun addFlight(
        request: CreateFlightRequest,
        snapshot: ByteArray
    ): Result<CreateFlightResponse> {
        return safeCall {
            getClient(tokenProvider).post {
                url {
                    path("/flights/create")
                }

                setBody(MultiPartFormDataContent(
                    formData {
                        append("metadata", Json.encodeToString(request))
//                        append() {
//                            contentType()
//                            append(HttpHeaders.ContentDisposition, "form-data; name=\"metadata\"")
//                        }

                        append("image", snapshot, Headers.build {
                            contentType(ContentType.Image.JPEG)
                            append(HttpHeaders.ContentDisposition, "form-data; name=\"image\"; filename=\"${Uuid.random()}.jpg\"")
                        })
                    }
                ))
            }
        }
    }

    suspend fun getAirports(): Result<AirportListResponse> {
        return safeCall {
            getClient(tokenProvider).get {
                url {
                    path("/airports/list")
                }
            }
        }
    }
}