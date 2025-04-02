package com.serrriy.aviascan

import com.serrriy.aviascan.database.DatabaseFactory
import com.serrriy.aviascan.database.AirportRepository
import com.serrriy.aviascan.routes.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    DatabaseFactory.init(environment.config)
    val airportRepo = AirportRepository("airports.csv")

    routing {
        userRoutes()
        flightRoutes(airportRepo)
        airportRoutes(airportRepo)
    }
}
