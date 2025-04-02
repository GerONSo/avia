package com.serrriy.aviascan.database

import com.serrriy.aviascan.data.airports.AirportDto
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.util.UUID

class AirportRepository(private val airportsFilename: String) {
    val airports: Map<String, AirportDto>

    init {
        val inputStream = this::class.java.classLoader.getResourceAsStream(airportsFilename)
            ?: throw IllegalArgumentException("File $airportsFilename not found in resources")

        airports = csvReader().readAllWithHeader(inputStream).associate { row ->
            val code = row["code"] ?: throw IllegalArgumentException("Missing airport code")
            code to AirportDto(
                name = row["name"] ?: throw IllegalArgumentException("Missing airport name"),
                code = code,
                city = row["city"] ?: throw IllegalArgumentException("Missing airport city"),
                country = row["country"] ?: throw IllegalArgumentException("Missing airport country")
            )
        }
    }

    fun list(): List<AirportDto> = airports.values.toList()
    fun getByCode(code: String): AirportDto = airports[code] ?: throw NoSuchElementException("Airport not found for code: $code")
}
