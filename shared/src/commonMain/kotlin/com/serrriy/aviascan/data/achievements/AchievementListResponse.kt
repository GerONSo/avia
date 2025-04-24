package com.serrriy.aviascan.data.achievements

import kotlinx.serialization.Serializable
import com.serrriy.aviascan.data.achievements.AchievementResponseDto

@Serializable
data class AchievementListResponse(
    val achievementsList: List<AchievementResponseDto>,
)