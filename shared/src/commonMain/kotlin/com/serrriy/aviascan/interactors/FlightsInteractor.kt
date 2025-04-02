package com.serrriy.aviascan.interactors

import com.serrriy.aviascan.client
import com.serrriy.aviascan.data.airports.AirportDto
import com.serrriy.aviascan.data.flights.CreateFlightRequest
import com.serrriy.aviascan.data.flights.CreateFlightResponse
import com.serrriy.aviascan.data.flights.FlightListItemDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path

class FlightsInteractor {
    fun getAllFlights(): List<FlightListItemDto> {
        return listOf(
            FlightListItemDto(
                id = "1",
                departureAirport = AirportDto(
                    code = "BKK",
                    name = "Suvarnabhumi Airport",
                    city = "Bangkok",
                    country = "Thailand",
                    latitude = 13.68187665,
                    longitude = 100.74858027718852,
                ),
                arrivalAirport = AirportDto(
                    code = "AUH",
                    name = "Abu Dhabi International Airport",
                    city = "Abu Dhabi",
                    country = "UAE",
                    latitude = 24.43189915,
                    longitude = 54.641976680103,
                ),
                date = "2025-01-12T18:30",
                userId = "123",
            ),
            FlightListItemDto(
                id = "2",
                departureAirport = AirportDto(
                    code = "AUH",
                    name = "Abu Dhabi International Airport",
                    city = "Abu Dhabi",
                    country = "UAE",
                    latitude = 24.43189915,
                    longitude = 54.641976680103,
                ),
                date = "2025-01-12T18:30",
                arrivalAirport = AirportDto(
                    code = "LHR",
                    name = "London Heathrow Airport",
                    city = "London",
                    country = "UK",
                    latitude = 51.46773895,
                    longitude = -0.4587800741571181,
                ),
                userId = "123",
            ),
        )
    }

    suspend fun addFlight(request: CreateFlightRequest): CreateFlightResponse {
        return client.get {
            url {
                protocol = URLProtocol.HTTP
                host = "jsonplaceholder.typicode.com"
                path("/posts")
            }
//            url {
//                host = "http://127.0.0.1:8080"
//                path("flights/create")
//            }
//            contentType(ContentType.Application.Json)
//            setBody(request)
        }.body()
    }
}