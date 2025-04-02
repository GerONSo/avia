package com.serrriy.aviascan.database

import com.serrriy.aviascan.models.AchievementDto
import com.serrriy.aviascan.database.Users
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object Achievements : UUIDTable() {
    val name = text("name")
    val imageUrl = text("image_url")
    val isHidden = bool("is_hidden")
    val text = text("text")
}

object UserAchievements : UUIDTable("user_achievements") {
    val user = reference("user_id", Users)
    val achievement = reference("achievement_id", Achievements)
}

class AchievementRepository {
    fun addUserAchievements(achievementIds: List<String>, userId: String) = transaction {
        for (achievementId in achievementIds) {
            UserAchievements.insert {
                it[UserAchievements.id] = UUID.randomUUID()
                it[UserAchievements.user] = UUID.fromString(userId)
                it[UserAchievements.achievement] = UUID.fromString(achievementId)
            }
        }
    }

    private fun getUserAchievements(userId: String): List<AchievementDto> {
        return UserAchievements
            .join(Achievements, JoinType.INNER, UserAchievements.achievement, Achievements.id)
            .select { UserAchievements.user eq UUID.fromString(userId) }
            .map { it ->
                AchievementDto(
                    id = it[Achievements.id].toString(),
                    name = it[Achievements.name],
                    isHidden = it[Achievements.isHidden],
                    imageUrl = it[Achievements.imageUrl],
                    text = it[Achievements.text],
                )
            }
    }

    fun getByUserId(userId: String): List<AchievementDto> = transaction {
        getUserAchievements(userId)
    }

    fun getIncompleteByUserId(userId: String): List<AchievementDto> = transaction {
        val userAchievements = getUserAchievements(userId)
        val allActiveAchievements = Achievements
            .select { Achievements.isHidden eq false }
            .map { it ->
                AchievementDto(
                    id = it[Achievements.id].toString(),
                    name = it[Achievements.name],
                    isHidden = it[Achievements.isHidden],
                    imageUrl = it[Achievements.imageUrl],
                    text = it[Achievements.text],
                )
        }

        allActiveAchievements.filterNot { it in userAchievements } 
    }
}
