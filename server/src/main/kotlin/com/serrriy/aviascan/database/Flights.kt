package com.serrriy.aviascan.database

import com.serrriy.aviascan.models.FlightDto
import com.serrriy.aviascan.database.Users
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

object Flights : UUIDTable() {
    val airportFrom = text("airport_from")
    val airportTo = text("airport_to")
    val flightDepartureTime = datetime("flight_departure_time")
    val flightArrivalTime = datetime("flight_arrival_time")
    val flightDepartureTimeZone = text("flight_departure_timezone")
    val flightArrivalTimeZone = text("flight_arrival_timezone")
    val flightCode = text("flight_code")
    val userId = reference("user_id", Users)
}

class FlightRepository {
    fun create(
        airportFrom: String,
        airportTo: String,
        flightDepartureTime: LocalDateTime,
        flightArrivalTime: LocalDateTime,
        flightDepartureTimeZone: String,
        flightArrivalTimeZone: String,
        flightCode: String,
        userId: String,
    ): FlightDto = transaction {
        val id = UUID.randomUUID()
        Flights.insert {
            it[Flights.id] = id
            it[Flights.airportFrom] = airportFrom
            it[Flights.airportTo] = airportTo
            it[Flights.flightDepartureTime] = flightDepartureTime
            it[Flights.flightArrivalTime] = flightArrivalTime
            it[Flights.flightDepartureTimeZone] = flightDepartureTimeZone
            it[Flights.flightArrivalTimeZone] = flightArrivalTimeZone
            it[Flights.flightCode] = flightCode
            it[Flights.userId] = UUID.fromString(userId)
        }
        FlightDto(
            id = id.toString(),
            airportFrom = airportFrom,
            airportTo = airportTo,
            flightDepartureTime = flightDepartureTime.atZone(ZoneId.of(flightDepartureTimeZone)).toOffsetDateTime(),
            flightArrivalTime = flightArrivalTime.atZone(ZoneId.of(flightArrivalTimeZone)).toOffsetDateTime(),
            flightCode = flightCode,
            userId = userId,
        )
    }

    fun getByUserId(userId: String): List<FlightDto> = transaction {
        Flights.select { Flights.userId eq UUID.fromString(userId) }
            .map { row -> 
                val departureTimeZone = row[Flights.flightDepartureTimeZone]
                val arrivalTimeZone = row[Flights.flightArrivalTimeZone]
                FlightDto(
                    id = row[Flights.id].toString(),
                    airportFrom = row[Flights.airportFrom],
                    airportTo = row[Flights.airportTo],
                    flightDepartureTime = row[Flights.flightDepartureTime].atZone(ZoneId.of(departureTimeZone)).toOffsetDateTime(),
                    flightArrivalTime = row[Flights.flightArrivalTime].atZone(ZoneId.of(arrivalTimeZone)).toOffsetDateTime(),
                    flightCode = row[Flights.flightCode],
                    userId = row[Flights.userId].toString(),
                )
            }
    }
}
