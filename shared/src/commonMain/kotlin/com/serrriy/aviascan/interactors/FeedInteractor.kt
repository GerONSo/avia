package com.serrriy.aviascan.interactors

import com.serrriy.aviascan.data.airports.AirportDto
import com.serrriy.aviascan.data.feed.FeedListItemDto
import com.serrriy.aviascan.data.feed.FeedListItemProfileDto
import com.serrriy.aviascan.data.flights.FlightListItemDto

class FeedInteractor {
    fun getFeed(): List<FeedListItemDto> {
        return listOf(
            FeedListItemDto(
                profile = FeedListItemProfileDto(
                    avatar = "",
                    name = "Sergey Goncharov",
                    datetime = "2025-01-12T13:40",
                ),
                mapSnapshot = "https://i.ibb.co/DHDcQQ3c/photo-2025-03-02-23-37-46.jpg",
                title = "Finally got myself to Serbia",
                flight = FlightListItemDto(
                    id = "1",
                    departureAirport = AirportDto(
                        "DME",
                        "Moscow Domodedovo Airport",
                        city = "Moscow",
                        country = "Russia",
                        latitude = 55.40912105,
                        longitude = 37.904166212415895
                    ),
                    date = "2025-01-12T13:40",
                    arrivalAirport = AirportDto(
                        "BEG",
                        "Belgrad Nikola Tesla Airport",
                        city = "Belgrade",
                        country = "Serbia",
                        latitude = 55.40912105,
                        longitude = 37.904166212415895
                    ),
                    userId = "123"
                )
            ),
            FeedListItemDto(
                profile = FeedListItemProfileDto(
                    avatar = "",
                    name = "Sergey Goncharov",
                    datetime = "2025-01-12T13:40",
                ),
                mapSnapshot = "",
                title = "Finally got myself to Serbia",
                flight = FlightListItemDto(
                    id = "2",
                    departureAirport = AirportDto(
                        "DME",
                        "Moscow Domodedovo Airport",
                        city = "Moscow",
                        country = "Russia",
                        latitude = 55.40912105,
                        longitude = 37.904166212415895
                    ),
                    date = "2025-01-12T13:40",
                    arrivalAirport = AirportDto(
                        "BEG",
                        "Belgrad Nikola Tesla Airport",
                        city = "Belgrade",
                        country = "Serbia",
                        latitude = 55.40912105,
                        longitude = 37.904166212415895
                    ),
                    userId = "345"
                )
            )
        )
    }

    fun getOwnFeed(): List<FeedListItemDto> {
        return listOf(
            FeedListItemDto(
                profile = FeedListItemProfileDto(
                    avatar = "",
                    name = "Sergey Goncharov",
                    datetime = "2025-01-12T13:40",
                ),
                mapSnapshot = "https://i.ibb.co/DHDcQQ3c/photo-2025-03-02-23-37-46.jpg",
                title = "Finally got myself to Serbia",
                flight = FlightListItemDto(
                    id = "1",
                    departureAirport = AirportDto(
                        "DME",
                        "Moscow Domodedovo Airport",
                        city = "Moscow",
                        country = "Russia",
                        latitude = 55.40912105,
                        longitude = 37.904166212415895
                    ),
                    date = "2025-01-12T13:40",
                    arrivalAirport = AirportDto(
                        "BEG",
                        "Belgrad Nikola Tesla Airport",
                        city = "Belgrade",
                        country = "Serbia",
                        latitude = 55.40912105,
                        longitude = 37.904166212415895
                    ),
                    userId = "123"
                )
            ),
        )
    }
}