package com.serrriy.aviascan.models

data class AchievementDto(
    val id: String,
    val name: String,
    val isHidden: Boolean,
    val imageUrl: String,
    val text: String?
)
