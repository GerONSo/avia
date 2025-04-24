package com.serrriy.aviascan.main.registration

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

class RegistrationViewModel(
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

    private val usersInteractor = UsersInteractor(
        tokenProvider = TokenProviderImpl(dataStoreRepository = dataStoreRepository)
    )

    private val _uiState = MutableStateFlow(MainState())
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

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

    fun repeatPasswordChanged(repeatPassword: String) {
        _uiState.update { currentState ->
            currentState.copy(repeatPassword = repeatPassword)
        }
    }

    fun loginChanged(login: String) {
        _uiState.update { currentState ->
            currentState.copy(login = login)
        }
    }

    fun registerButtonPressed() {
        viewModelScope.launch {
            usersInteractor.register(
                email = uiState.value.email,
                name = uiState.value.login,
                password = uiState.value.password,
            ).onSuccess { result ->
                dataStoreRepository.writeToDataStore(DataStoreKeys.accessToken, result.token.accessToken)
                dataStoreRepository.writeToDataStore(DataStoreKeys.refreshToken, result.token.refreshToken ?: "")
                dataStoreRepository.writeToDataStore(DataStoreKeys.userId, result.userId)
                _uiState.update { currentState ->
                    currentState.copy(
                        registerSuccess = true
                    )
                }
            }.onFailure {

            }

        }

        _uiState.update { currentState ->
            currentState.copy(registerButtonPressedOnce = true)
        }
    }
}

data class MainState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val login: String = "",
    val registerButtonPressedOnce: Boolean = false,
    val registerSuccess: Boolean = false,
) {
    val passwordMatch: Boolean
        get() = password == repeatPassword

    val registerButtonActive: Boolean
        get() = email.isNotEmpty() && password.isNotEmpty() && login.isNotEmpty() && repeatPassword.isNotEmpty() && passwordMatch
}