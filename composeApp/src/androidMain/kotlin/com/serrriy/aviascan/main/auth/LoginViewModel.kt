package com.serrriy.aviascan.main.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serrriy.aviascan.interactors.UsersInteractor
import com.serrriy.aviascan.repositories.DataStoreKeys
import com.serrriy.aviascan.repositories.DataStoreRepository
import com.serrriy.aviascan.utils.TokenProviderImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class LoginViewModel(
    dataStoreRepository: DataStoreRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    private val usersInteractor = UsersInteractor(
        tokenProvider = TokenProviderImpl(dataStoreRepository = dataStoreRepository)
    )
    private lateinit var dataStoreRepository: DataStoreRepository

    fun emailChanged(email: String) {
        _uiState.update { currentState ->
            currentState.copy(email = email)
        }
    }

    fun passwordChanged(password: String) {
        _uiState.update { currentState ->
            currentState.copy(password = password)
        }
    }

    fun loginButtonClicked() {
        viewModelScope.launch {
            usersInteractor.login(uiState.value.email, uiState.value.password)
                .onSuccess { result ->
                    dataStoreRepository.writeToDataStore(DataStoreKeys.accessToken, result.token.accessToken)
                    dataStoreRepository.writeToDataStore(DataStoreKeys.refreshToken, result.token.refreshToken ?: "")
                    dataStoreRepository.writeToDataStore(DataStoreKeys.userId, result.userId)
                    _uiState.update { currentState ->
                        currentState.copy(loginSuccess = true)
                    }
                }.onFailure {

                }
        }
    }

    fun initDataStore(context: Context) {
        dataStoreRepository = DataStoreRepository(context)
    }

    fun clearLoginSuccess() {
        _uiState.update { currentState ->
            currentState.copy(loginSuccess = false)
        }
    }
}

data class LoginState(
    val email: String = "",
    val password: String = "",
    val loginSuccess: Boolean = false,
)