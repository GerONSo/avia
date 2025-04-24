package com.serrriy.aviascan.routes

import com.serrriy.aviascan.database.AirportRepository
import com.serrriy.aviascan.data.airports.*
import com.serrriy.aviascan.data.IDResponse
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import org.jetbrains.exposed.sql.*


fun Route.airportRoutes(airportRepo: AirportRepository) {
    route("/airports") {
        authenticate("auth-jwt") {
            get("/list") {
                call.respond(HttpStatusCode.OK, AirportListResponse(airportRepo.list()))
            }
        }
    }
}
