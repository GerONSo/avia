package com.serrriy.aviascan.processors

import com.serrriy.aviascan.database.AirportRepository
import com.serrriy.aviascan.models.*
import com.serrriy.aviascan.utils.calculateDistance
import java.time.Duration


class AchievementsProcessor(private val airportsRepo: AirportRepository) {
    fun distance(flights: List<FlightDto>): Boolean {
        return flights.sumOf { 
            val airportFrom = airportsRepo.getByCode(it.airportFrom)
            val airportTo = airportsRepo.getByCode(it.airportTo)
            calculateDistance(
                airportFrom.latitude, airportFrom.longitude, airportTo.latitude, airportTo.longitude
            )
        } > 10000.0
    }

    fun time(flights: List<FlightDto>): Boolean {
        val totalFlightTime = flights.sumOf { 
            Duration.between(it.flightDepartureTime.toInstant(), it.flightArrivalTime.toInstant()).toHours()
        }
        return totalFlightTime > 10.0
    }

    fun russia(flights: List<FlightDto>): Boolean {
        return flights.any { airportsRepo.getByCode(it.airportTo).country == "RU" }
    }

    fun france(flights: List<FlightDto>): Boolean {
        return flights.any { airportsRepo.getByCode(it.airportTo).country == "FR" }
    }

    fun uk(flights: List<FlightDto>): Boolean {
        return flights.any { airportsRepo.getByCode(it.airportTo).country == "GB" }
    }

    fun us(flights: List<FlightDto>): Boolean {
        return flights.any { airportsRepo.getByCode(it.airportTo).country == "US" }
    }

    fun mexico(flights: List<FlightDto>): Boolean {
        return flights.any { airportsRepo.getByCode(it.airportTo).country == "MX" }
    }

    fun germany(flights: List<FlightDto>): Boolean {
        return flights.any { airportsRepo.getByCode(it.airportTo).country == "DE" }
    }


    // name of achievement to achievement success function
    val methodMap: Map<String, (flights: List<FlightDto>) -> Boolean> = mapOf(
        "distance" to ::distance,
        "time" to ::time,
        "russia" to ::russia,
        "uk" to ::uk,
        "usa" to ::us,
        "france" to ::france,
        "mexico" to ::mexico,
        "germany" to ::germany,
    )

    fun processNewFlight(flights: List<FlightDto>, incompleteAchievements: List<AchievementDto>): List<AchievementDto> {
        return incompleteAchievements.mapNotNull { achievement ->
            val func = methodMap[achievement.name] ?: throw NoSuchElementException("Achievement function not found for name: ${achievement.name}, ${methodMap.keys}")
            if (func(flights)) achievement else null
        }
    }
}