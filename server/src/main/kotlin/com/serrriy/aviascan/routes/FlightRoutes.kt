package com.serrriy.aviascan.routes

import com.serrriy.aviascan.database.FlightRepository
import com.serrriy.aviascan.database.AirportRepository
import com.serrriy.aviascan.database.ImageRepository
import com.serrriy.aviascan.database.AchievementRepository
import com.serrriy.aviascan.processors.AchievementsProcessor
import com.serrriy.aviascan.database.PostRepository
import com.serrriy.aviascan.data.achievements.*
import com.serrriy.aviascan.data.flights.*
import com.serrriy.aviascan.data.airports.*
import com.serrriy.aviascan.routes.decodeMultipartForm
import com.serrriy.aviascan.utils.calculateDistance
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import org.jetbrains.exposed.sql.*
import java.time.LocalDateTime

fun Route.flightRoutes(airportRepo: AirportRepository, imageRepo: ImageRepository) {
    route("/flights") {
        // Add a new user
        authenticate("auth-jwt") {
            post("/create") {
                val multipart = call.receiveMultipart()
                val (createFlight, imageUpload) = decodeMultipartForm<CreateFlightRequest>(multipart)
                if (imageUpload == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }

                val flightRepo = FlightRepository()
                val achievementRepo = AchievementRepository()
                val postRepo = PostRepository()

                val flight = flightRepo.create(
                    airportFrom = createFlight.departure.airport,
                    airportTo = createFlight.arrival.airport,
                    flightDepartureTime = LocalDateTime.parse(createFlight.departure.flightTime),
                    flightArrivalTime = LocalDateTime.parse(createFlight.arrival.flightTime),
                    flightDepartureTimeZone = createFlight.departure.timeZone,
                    flightArrivalTimeZone = createFlight.arrival.timeZone,
                    flightCode = createFlight.flightCode,
                    userId = createFlight.userId,
                )

                val imageUrl = imageRepo.uploadImage(imageUpload.bytes, imageUpload.filename, basePath = "routes")
                postRepo.create(createFlight.userId, flight.id, imageUrl)

                val flights = flightRepo.getByUserId(createFlight.userId)
                val incompleteAchievements = achievementRepo.getIncompleteByUserId(createFlight.userId)

                val achievementsToAdd = AchievementsProcessor(airportRepo).processNewFlight(flights, incompleteAchievements)
                achievementRepo.addUserAchievements(achievementsToAdd.map { it.id }, createFlight.userId)
                
                val response = CreateFlightResponse(achievementsToAdd.map { AchievementResponseDto(it.imageUrl, it.text) })
                call.respond(HttpStatusCode.OK, response)
            }
        }

        authenticate("auth-jwt") {
            get("/list/{id}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val flights = FlightRepository().getByUserId(id).map { flight ->
                    val departureAirport = airportRepo.getByCode(flight.airportFrom)
                    val arrivalAirport = airportRepo.getByCode(flight.airportTo)
                    FlightListItemDto(
                        id = flight.id, 
                        departureAirport = departureAirport,
                        arrivalAirport = arrivalAirport,
                        flightDepartureTime = flight.flightDepartureTime.toString(),
                        flightArrivalTime = flight.flightArrivalTime.toString(),
                        distance = calculateDistance(
                            departureAirport.latitude, departureAirport.longitude, arrivalAirport.latitude, arrivalAirport.longitude, 
                        ),
                        userId = flight.userId
                    )
                }
                call.respond(HttpStatusCode.OK, FlightListResponse(flights))
            }
        }
    }
}
