package com.serrriy.aviascan.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serrriy.aviascan.data.achievements.AchievementResponseDto
import com.serrriy.aviascan.interactors.AchievementInteractor
import com.serrriy.aviascan.interactors.UsersInteractor
import com.serrriy.aviascan.profile.achievements.data.AchievementListItem
import com.serrriy.aviascan.profile.achievements.data.toAchievementListItem
import com.serrriy.aviascan.repositories.DataStoreKeys.userId
import com.serrriy.aviascan.repositories.DataStoreRepository
import com.serrriy.aviascan.utils.TokenProviderImpl
import com.serrriy.aviascan.utils.withUserIdFrom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Collections

class ProfileViewModel(
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

    private val tokenProvider = TokenProviderImpl(dataStoreRepository)
    private val achievementInteractor = AchievementInteractor(tokenProvider)
    private val usersInteractor = UsersInteractor(tokenProvider)

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    fun getAchievements() {
        viewModelScope.launch {
            dataStoreRepository.readFromDataStore(userId)?.let { userId ->
                val achievementsList = achievementInteractor.getAllAchievements(
                    userId = userId
                ).achievementsList
                    .map(AchievementResponseDto::toAchievementListItem)
                _uiState.update { currentState ->
                    currentState.copy(
                        achievementsList = achievementsList
                    )
                }
            }
        }
    }

    fun getUserName() {
        viewModelScope.launch {
            withUserIdFrom(dataStoreRepository) { userId ->
                usersInteractor.profile(userId).onSuccess { result ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            userName = result.name,
                            lastUserName = result.name,
                            email = result.email,
                        )
                    }
                }.onFailure {

                }

            }
        }
    }

    fun updateName(newName: String) {
        _uiState.update { currentState ->
            currentState.copy(userName = newName)
        }
    }

    fun changeName() {
        viewModelScope.launch {
            withUserIdFrom(dataStoreRepository) { userId ->
                usersInteractor.changeUserInfo(userId, uiState.value.userName).onSuccess {
                    _uiState.update { currentState ->
                        currentState.copy(
                            lastUserName = uiState.value.userName,
                        )
                    }
                }.onFailure {

                }
            }
        }
    }
}

data class ProfileState(
    val userName: String = "",
    val lastUserName: String = "",
    val email: String? = "",
    val achievementsList: List<AchievementListItem> = Collections.emptyList(),
)