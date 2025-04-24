package com.serrriy.aviascan.routes

import com.serrriy.aviascan.database.AirportRepository
import com.serrriy.aviascan.database.PostRepository
import com.serrriy.aviascan.database.UserRepository
import com.serrriy.aviascan.data.posts.*
import com.serrriy.aviascan.data.user.UserResponse
import com.serrriy.aviascan.data.feed.*
import com.serrriy.aviascan.data.flights.FlightListItemDto
import com.serrriy.aviascan.utils.calculateDistance
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import org.jetbrains.exposed.sql.*


fun Route.postRoutes(airportRepo: AirportRepository) {
    route("/posts") {
        authenticate("auth-jwt") {
            post("/publish") {
                val publishPost = call.receive<PostPublishRequest>()
                val postRepo = PostRepository()

                postRepo.update(id = publishPost.id, title = publishPost.title, isPublished = true)
                call.respond(HttpStatusCode.OK)
            }
        }

        authenticate("auth-jwt") {
            get("/list/{userId}") {
                val userId = call.parameters["userId"]
                if (userId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val postRepo = PostRepository()
                val user = UserRepository().userById(userId)
                user ?: run {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                val userResponse = UserResponse(id = user.id, name = user.name, imageUrl = user.imageUrl)

                val userPosts = postRepo.getByUserId(userId = userId).map { (post, flight) -> 
                    val departureAirport = airportRepo.getByCode(flight.airportFrom)
                    val arrivalAirport = airportRepo.getByCode(flight.airportTo)
                    val responseFlight = FlightListItemDto(
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
                    FeedListItemDto(user = userResponse, flight = responseFlight, post = post)
                }
                call.respond(HttpStatusCode.OK, FeedListResponse(userPosts))
            }
        }

        authenticate("auth-jwt") {
            get("/feed/{userId}") {
                val userId = call.parameters["userId"]
                if (userId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val postRepo = PostRepository()
                val user = UserRepository().userById(userId)
                user ?: run {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                val userPosts = postRepo.getFeedUserId(userId = userId).map { (post, flight, user) -> 
                    val departureAirport = airportRepo.getByCode(flight.airportFrom)
                    val arrivalAirport = airportRepo.getByCode(flight.airportTo)
                    val responseFlight = FlightListItemDto(
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
                    val responseUser = UserResponse(id = user.id, name = user.name, imageUrl = user.imageUrl)
                    FeedListItemDto(user = responseUser, flight = responseFlight, post = post)
                }
                call.respond(HttpStatusCode.OK, FeedListResponse(userPosts))
            }

        }
    }
}
