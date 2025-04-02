package com.serrriy.aviascan.routes

import com.serrriy.aviascan.database.FlightRepository
import com.serrriy.aviascan.database.AirportRepository
import com.serrriy.aviascan.database.AchievementRepository
import com.serrriy.aviascan.processors.AchievementsProcessor
import com.serrriy.aviascan.data.achievements.*
import com.serrriy.aviascan.data.flights.*
import com.serrriy.aviascan.data.airports.*
import com.serrriy.aviascan.data.IDResponse
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import org.jetbrains.exposed.sql.*

fun Route.flightRoutes(airportRepo: AirportRepository) {
    route("/flights") {
        // Add a new user
        post("/create") {
            val createFlight = call.receive<CreateFlightRequest>()
            val flightRepo = FlightRepository()
            val achievementRepo = AchievementRepository()
            flightRepo.create(
                airportFrom = createFlight.departureAirport,
                airportTo = createFlight.arrivalAirport,
                date = createFlight.flightDate,
                flightCode = createFlight.flightCode,
                userId = createFlight.userId,
                flightDistance = createFlight.flightDistance,
                flightTime = createFlight.flightTime,
            )
            val flights = flightRepo.getByUserId(createFlight.userId)
            val incompleteAchievements = achievementRepo.getIncompleteByUserId(createFlight.userId)

            val achievementsToAdd = AchievementsProcessor().processNewFlight(flights, incompleteAchievements)
            achievementRepo.addUserAchievements(achievementsToAdd.map { it.id }, createFlight.userId)
            
            val response = CreateFlightResponse(achievementsToAdd.map { AchievementResponseDto(it.imageUrl, it.text) })
            call.respond(HttpStatusCode.OK, response)
        }

        get("/list") {
            val flights = FlightRepository().list().map { flight ->
                FlightListItemDto(
                    id = flight.id, 
                    departureAirport = airportRepo.getByCode(flight.airportFrom),
                    arrivalAirport = airportRepo.getByCode(flight.airportFrom),
                    date = flight.date,
                    userId = flight.userId
                )
            }
            call.respond(HttpStatusCode.OK, FlightListResponse(flights))
        }
    }
}
