package com.serrriy.aviascan.data.achievements

import kotlinx.serialization.Serializable

@Serializable
data class AchievementResponseDto(
    val image: String,
    val title: String?,
)