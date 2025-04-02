package com.serrriy.aviascan.achievements

import androidx.lifecycle.ViewModel
import com.serrriy.aviascan.achievements.data.AchievementListItem
import com.serrriy.aviascan.achievements.data.toAchievementListItem
import com.serrriy.aviascan.data.achievements.AchievementResponseDto
import com.serrriy.aviascan.interactors.AchievementInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Collections

class AchievementsViewModel : ViewModel() {
    private val achievementInteractor = AchievementInteractor()

    private val _uiState = MutableStateFlow(AchievementsState())
    val uiState: StateFlow<AchievementsState> = _uiState.asStateFlow()

    init {
        getAchievements()
    }

    fun getAchievements() {
        _uiState.update { currentState ->
            currentState.copy(
                achievementsList = achievementInteractor.getAllAchievements()
                    .map(AchievementResponseDto::toAchievementListItem)
            )
        }
    }

}

data class AchievementsState(
    val achievementsList: List<AchievementListItem> = Collections.emptyList(),
)