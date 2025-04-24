package com.serrriy.aviascan

import com.serrriy.aviascan.database.DatabaseFactory
import com.serrriy.aviascan.database.AirportRepository
import com.serrriy.aviascan.database.ImageRepository
import com.serrriy.aviascan.routes.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.response.*
import com.serrriy.aviascan.utils.JwtConfig
import io.ktor.server.auth.jwt.*
import io.ktor.server.auth.*
import io.ktor.http.*
import io.ktor.server.routing.*
import java.util.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(JwtConfig.getVerifier())
            validate { credential ->
                if (credential.payload.expiresAt.after(Date()) && credential.payload.getClaim("type").asString() == "access") {
                    JWTPrincipal(credential.payload)
                } else null
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is invalid or expired")
            }
        }
    }

    DatabaseFactory.init(environment.config)
    val airportRepo = AirportRepository("airports.csv")
    val imageRepo = ImageRepository(
        bucket = "aviascan-public",
        accessKey = System.getenv("ACCESS_KEY"),
        region  = "ru-central1-a",
        secretKey = System.getenv("SECRET_KEY"),
        endpointUrl = "https://storage.yandexcloud.net"
    )

    routing {
        userRoutes(imageRepo)
        flightRoutes(airportRepo, imageRepo)
        airportRoutes(airportRepo)
        achievementRoutes()
        postRoutes(airportRepo)
    }
}
