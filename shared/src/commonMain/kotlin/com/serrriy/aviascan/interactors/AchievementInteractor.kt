package com.serrriy.aviascan.interactors

import com.serrriy.aviascan.data.achievements.AchievementResponseDto

class AchievementInteractor {
    fun getAllAchievements(): List<AchievementResponseDto> {
        return listOf(
            AchievementResponseDto(
                image = "https://storage.yandexcloud.net/aviascan-public/achievements/distance_h.png",
                title = "Spent 10000 miles in the air"
            ),
            AchievementResponseDto(
                image = "https://storage.yandexcloud.net/aviascan-public/achievements/time_n.png",
                title = "Not yet achieved"
            ),
            AchievementResponseDto(
                image = "https://storage.yandexcloud.net/aviascan-public/achievements/time_n.png",
                title = "Not yet achieved"
            ),
        )
    }
}