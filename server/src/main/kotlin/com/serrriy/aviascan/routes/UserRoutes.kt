package com.serrriy.aviascan.routes

import com.serrriy.aviascan.database.UserRepository
import com.serrriy.aviascan.data.user.*
import com.serrriy.aviascan.data.IDResponse
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import org.jetbrains.exposed.sql.*

fun Route.userRoutes() {
    route("/users") {
        post("/create") {
            val createUser = call.receive<CreateUserRequest>()
            val user = UserRepository().create(
                name = createUser.name,
                firebaseId = createUser.firebaseId
            )
            call.respond(HttpStatusCode.Created, IDResponse(user.id))
        }
    }
}
