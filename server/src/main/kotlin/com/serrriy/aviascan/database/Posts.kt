package com.serrriy.aviascan.database

import com.serrriy.aviascan.database.Users
import com.serrriy.aviascan.database.UserSubscriptions
import com.serrriy.aviascan.database.Flights
import com.serrriy.aviascan.data.posts.PostDto
import com.serrriy.aviascan.data.user.UserDto
import com.serrriy.aviascan.models.FlightDto
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId


object Posts : UUIDTable() {
    val user = reference("user_id", Users)
    val flight = reference("flight_id", Flights)
    val title = text("title").nullable()
    val imageUrl = text("image_url").nullable()
    val createdAt: Column<java.time.LocalDateTime> = datetime("created_at")
    val isPublished = bool("is_published")
}


class PostRepository {
    fun create(userId: String, flightId: String, imageUrl: String) = transaction {
        Posts.insert {
            it[Posts.user] = UUID.fromString(userId)
            it[Posts.flight] = UUID.fromString(flightId)
            it[Posts.title] = null
            it[Posts.imageUrl] = imageUrl
            it[Posts.isPublished] = false
        }
    }

    fun update(id: String, title: String, isPublished: Boolean = true) = transaction {
        Posts.update ({ Posts.id eq UUID.fromString(id) }) {
            it[Posts.title] = title
            it[Posts.isPublished] = isPublished
        }
    }

    fun getByUserId(userId: String): List<Pair<PostDto, FlightDto>> = transaction {
        Posts
            .join(Flights, JoinType.INNER, Posts.flight, Flights.id)
            .select { Posts.user eq UUID.fromString(userId) }
            .orderBy(Posts.createdAt to SortOrder.DESC)
            .map { row -> 
                val post = PostDto(
                    id = row[Posts.id].toString(),
                    userId = row[Posts.user].toString(),
                    flightId = row[Posts.flight].toString(),
                    imageUrl = row[Posts.imageUrl],
                    createdAt = row[Posts.createdAt].toString(),
                    isPublished = row[Posts.isPublished],
                    title = row[Posts.title],
                )
                val departureTimeZone = row[Flights.flightDepartureTimeZone]
                val arrivalTimeZone = row[Flights.flightArrivalTimeZone]
                val flight = FlightDto(
                    id = row[Flights.id].toString(),
                    airportFrom = row[Flights.airportFrom],
                    airportTo = row[Flights.airportTo],
                    flightDepartureTime = row[Flights.flightDepartureTime].atZone(ZoneId.of(departureTimeZone)).toOffsetDateTime(),
                    flightArrivalTime = row[Flights.flightArrivalTime].atZone(ZoneId.of(arrivalTimeZone)).toOffsetDateTime(),
                    flightCode = row[Flights.flightCode],
                    userId = row[Flights.userId].toString(),
                )
                Pair(post, flight)
            }
    }

    fun getFeedUserId(userId: String): List<Triple<PostDto, FlightDto, UserDto>> = transaction {
        val userSubscriptions = UserSubscriptions
            .slice(UserSubscriptions.subscribedTo)
            .select { UserSubscriptions.user eq UUID.fromString(userId) }

        Posts
            .join(Flights, JoinType.INNER, Posts.flight, Flights.id)
            .join(Users, JoinType.INNER, Posts.user, Users.id)
            .select { Posts.isPublished and (Posts.user eq UUID.fromString(userId) or (Users.id inSubQuery userSubscriptions)) }
            .orderBy(Posts.createdAt to SortOrder.DESC)
            .map { row -> 
                val post = PostDto(
                    id = row[Posts.id].toString(),
                    userId = row[Posts.user].toString(),
                    flightId = row[Posts.flight].toString(),
                    imageUrl = row[Posts.imageUrl],
                    createdAt = row[Posts.createdAt].toString(),
                    isPublished = row[Posts.isPublished],
                    title = row[Posts.title],
                )
                val departureTimeZone = row[Flights.flightDepartureTimeZone]
                val arrivalTimeZone = row[Flights.flightArrivalTimeZone]
                val flight = FlightDto(
                    id = row[Flights.id].toString(),
                    airportFrom = row[Flights.airportFrom],
                    airportTo = row[Flights.airportTo],
                    flightDepartureTime = row[Flights.flightDepartureTime].atZone(ZoneId.of(departureTimeZone)).toOffsetDateTime(),
                    flightArrivalTime = row[Flights.flightArrivalTime].atZone(ZoneId.of(arrivalTimeZone)).toOffsetDateTime(),
                    flightCode = row[Flights.flightCode],
                    userId = row[Flights.userId].toString(),
                )
                val user = UserDto(
                    id = row[Users.id].toString(),
                    name = row[Users.name],
                    email = row[Users.email],
                    imageUrl = row[Users.imageUrl],
                    password = row[Users.password],
                )
                Triple(post, flight, user)
            }
    }
}
