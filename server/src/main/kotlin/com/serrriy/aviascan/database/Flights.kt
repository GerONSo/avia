package com.serrriy.aviascan.database

import com.serrriy.aviascan.models.FlightDto
import com.serrriy.aviascan.database.Users
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object Flights : UUIDTable() {
    val airportFrom = text("airport_from")
    val airportTo = text("airport_to")
    val date = text("date")
    val flightCode = text("flight_code")
    val userId = reference("user_id", Users)
    val flightDistance = float("flight_distance")
    val flightTime = float("flight_time")
}

class FlightRepository {
    fun create(
        airportFrom: String,
        airportTo: String,
        date: String,
        flightCode: String,
        userId: String,
        flightDistance: Float,
        flightTime: Float,
    ): FlightDto = transaction {
        val id = UUID.randomUUID()
        Flights.insert {
            it[Flights.id] = id
            it[Flights.airportFrom] = airportFrom
            it[Flights.airportTo] = airportTo
            it[Flights.date] = date
            it[Flights.flightCode] = flightCode
            it[Flights.userId] = UUID.fromString(userId)
            it[Flights.flightDistance] = flightDistance
            it[Flights.flightTime] = flightTime
        }
        FlightDto(
            id = id.toString(),
            airportFrom = airportFrom,
            airportTo = airportTo,
            date = date,
            flightCode = flightCode,
            userId = userId,
            flightDistance = flightDistance,
            flightTime = flightTime,
        )
    }

    fun list(): List<FlightDto> = transaction {
        Flights.selectAll().map { row -> 
            FlightDto(
                id = row[Flights.id].toString(),
                airportFrom = row[Flights.airportFrom],
                airportTo = row[Flights.airportTo],
                date = row[Flights.date],
                flightCode = row[Flights.flightCode],
                userId = row[Flights.userId].toString(),
                flightDistance = row[Flights.flightDistance],
                flightTime = row[Flights.flightTime],
            )
        }
    }

    fun getByUserId(userId: String): List<FlightDto> = transaction {
        Flights.select { Flights.userId eq UUID.fromString(userId) }
            .map { row -> 
                FlightDto(
                    id = row[Flights.id].toString(),
                    airportFrom = row[Flights.airportFrom],
                    airportTo = row[Flights.airportTo],
                    date = row[Flights.date],
                    flightCode = row[Flights.flightCode],
                    userId = row[Flights.userId].toString(),
                    flightDistance = row[Flights.flightDistance],
                    flightTime = row[Flights.flightTime],
                )
            }
    }
}
