package com.serrriy.aviascan.database

import com.serrriy.aviascan.data.user.UserDto
import com.serrriy.aviascan.database.Users.email
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.UUID

object Users : UUIDTable() {
    val name = text("name")
    val email = text("email")
    val imageUrl = text("image_url").nullable()
    val password = text("password")
}

object UserSubscriptions : UUIDTable("user_subscriptions") {
    val user = reference("user_id", Users)
    val subscribedTo = reference("subscribed_to_user_id", Users)
}

class UserRepository {
    fun create(name: String, email: String, encodedPassword: String): UserDto = transaction {
        val id = UUID.randomUUID()
        Users.insert {
            it[Users.id] = id
            it[Users.name] = name
            it[Users.email] = email
            it[Users.password] = encodedPassword
        }
        UserDto(id = id.toString(), name = name, email = email, password = encodedPassword, imageUrl = null)
    }

    private fun buildUserDtoFromRow(row: ResultRow): UserDto {
        return UserDto(
            id = row[Users.id].toString(),
            name = row[Users.name],
            email = row[email],
            imageUrl = row[Users.imageUrl],
            password = row[Users.password],
        )
    }

    fun getByEmail(email: String): UserDto? = transaction {
        Users.select { Users.email eq email }
            .map { buildUserDtoFromRow(it) }
            .firstOrNull()
    }

    fun listWithSubscriptions(
        id: String,
        query: String,
        startFrom: String,
        limit: Int,
        startFromSubscribed: Boolean = true
    ): Pair<List<UserDto>?, List<UserDto>?> = transaction {
        val userId = UUID.fromString(id)
        val startFromId = UUID.fromString(startFrom)
        val userSubscriptions = UserSubscriptions
            .slice(UserSubscriptions.subscribedTo)
            .select { UserSubscriptions.user eq userId }
        
        var usersSubscribed: List<UserDto>? = null
        if (startFromSubscribed) {
            usersSubscribed = Users
                .select { 
                    Users.id neq userId and 
                    (Users.id inSubQuery userSubscriptions and 
                    (Users.id greaterEq startFromId and 
                    (Users.name like "%${query}%")))
                }
                .orderBy(Users.id to SortOrder.ASC)
                .limit(limit)
                .map { buildUserDtoFromRow(it) }
        }
        var usersNotSubscribed: List<UserDto>? = null
        if (usersSubscribed == null) {
            usersNotSubscribed = Users
                .select { 
                    Users.id neq userId and 
                    (Users.id notInSubQuery userSubscriptions and 
                    (Users.id greaterEq startFromId and 
                    (Users.name like "%${query}%")))
                }
                .orderBy(Users.id to SortOrder.ASC)
                .limit(limit)
                .map { buildUserDtoFromRow(it) }
        } else if (usersSubscribed.size < limit) {
            usersNotSubscribed = Users
                .select { 
                    Users.id neq userId and 
                    (Users.id notInSubQuery userSubscriptions and 
                    (Users.name like "%${query}%"))
                }
                .orderBy(Users.id to SortOrder.ASC)
                .limit(limit - usersSubscribed.size)
                .map { buildUserDtoFromRow(it) }
        }
        return@transaction Pair(usersSubscribed, usersNotSubscribed)
    }

    fun subscribe(userId: String, subscribeTo: String) = transaction {
        UserSubscriptions.insert {
            it[UserSubscriptions.user] = UUID.fromString(userId)
            it[UserSubscriptions.subscribedTo] = UUID.fromString(subscribeTo)
        }
    }

    fun unsubscribe(userId: String, unsubscribeFrom: String) = transaction {
        UserSubscriptions.deleteWhere {
            (user eq UUID.fromString(userId)) and (subscribedTo eq UUID.fromString(unsubscribeFrom))
        }
    }

    fun userById(userId: String) = transaction {
        Users.select { Users.id eq UUID.fromString(userId) }
            .map { buildUserDtoFromRow(it) }
            .firstOrNull()
    }

    fun changeUserName(userId: String, newName: String, newImageUrl: String?) = transaction {
        Users.update({ Users.id eq UUID.fromString(userId) }) { it[name] = newName }
        if (newImageUrl != null) {
            Users.update({ Users.id eq UUID.fromString(userId) }) { it[imageUrl] = newImageUrl }
        }
    }
}
