package com.serrriy.aviascan.data.flights

import com.serrriy.aviascan.data.achievements.AchievementResponseDto
import kotlinx.serialization.Serializable

@Serializable
data class CreateFlightResponse(
    val achievementsList: List<AchievementResponseDto>,
)