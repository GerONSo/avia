package com.serrriy.aviascan.database

import com.serrriy.aviascan.data.user.UserDto
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.UUID

object Users : UUIDTable() {
    val name = text("name")
    val firebaseId = text("firebase_id")
}

class UserRepository {
    fun create(name: String, firebaseId: String): UserDto = transaction {
        val id = UUID.randomUUID()
        Users.insert {
            it[Users.id] = id
            it[Users.name] = name
            it[Users.firebaseId] = firebaseId
        }
        UserDto(id = id.toString(), name = name, firebaseId = firebaseId)
    }
}
