package com.serrriy.aviascan.achievements.data

import com.serrriy.aviascan.data.achievements.AchievementResponseDto

data class AchievementListItem(
    val image: String,
    val title: String,
)

fun AchievementResponseDto.toAchievementListItem() = AchievementListItem(
    image = image,
    title = title,
)