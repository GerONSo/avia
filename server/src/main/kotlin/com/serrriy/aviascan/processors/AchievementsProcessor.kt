package com.serrriy.aviascan.processors

import com.serrriy.aviascan.models.*


class AchievementsProcessor {
    fun distance(flights: List<FlightDto>): Boolean {
        return flights.sumOf { it.flightDistance.toDouble() } > 1000.0
    }

    fun time(flights: List<FlightDto>): Boolean {
        return flights.sumOf { it.flightTime.toDouble() } > 100.0
    }

    // name of achievement to achievement success function
    val methodMap: Map<String, (flights: List<FlightDto>) -> Boolean> = mapOf(
        "distance" to ::distance,
        "time" to ::time,
    )

    fun processNewFlight(flights: List<FlightDto>, incompleteAchievements: List<AchievementDto>): List<AchievementDto> {
        return incompleteAchievements.mapNotNull { achievement ->
            val func = methodMap[achievement.name] ?: throw NoSuchElementException("Achievement function not found for name: ${achievement.name}, ${methodMap.keys}")
            if (func(flights)) achievement else null
        }
    }
}