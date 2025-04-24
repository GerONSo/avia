package com.serrriy.aviascan.routes

import com.serrriy.aviascan.database.AchievementRepository
import com.serrriy.aviascan.data.achievements.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import org.jetbrains.exposed.sql.*


fun Route.achievementRoutes() {
    route("/achievements") {
        authenticate("auth-jwt") {
            get("/list/{id}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val achievementRepo = AchievementRepository()

                val userAchievements = achievementRepo.getByUserId(id)
                val hiddenAchievements = achievementRepo.getIncompleteByUserId(userId = id, getHidden = true)
                val achievements = userAchievements + hiddenAchievements
                val response = AchievementListResponse(achievements.map { AchievementResponseDto(it.imageUrl, it.text) })
                call.respond(HttpStatusCode.OK, response)
            }
        }
    }
}
