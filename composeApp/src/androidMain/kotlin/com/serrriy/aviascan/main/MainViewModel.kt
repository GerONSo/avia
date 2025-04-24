package com.serrriy.aviascan.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serrriy.aviascan.interactors.UsersInteractor
import com.serrriy.aviascan.repositories.DataStoreKeys.accessToken
import com.serrriy.aviascan.repositories.DataStoreKeys.refreshToken
import com.serrriy.aviascan.repositories.DataStoreKeys.userId
import com.serrriy.aviascan.repositories.DataStoreRepository
import com.serrriy.aviascan.utils.JWTUtils
import com.serrriy.aviascan.utils.TokenProviderImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    dataStoreRepository: DataStoreRepository,
) : ViewModel() {
    private lateinit var dataStoreRepository: DataStoreRepository
    private val jwtUtils by lazy { JWTUtils.getInstance(dataStoreRepository) }
    private val usersInteractor = UsersInteractor(
        tokenProvider = TokenProviderImpl(dataStoreRepository = dataStoreRepository)
    )

    private val _mainState = MutableStateFlow(MainState())
    val mainState: StateFlow<MainState> = _mainState.asStateFlow()

    fun initDataStore(context: Context) {
        dataStoreRepository = DataStoreRepository(context)
    }

    suspend fun processAuth() {
        when {
            jwtUtils.isAccessTokenValid() -> {
                // logged in
                _mainState.update { currentState ->
                    currentState.copy(authStatus = AuthStatus.LOGGED_IN)
                }
            }

            !jwtUtils.isAccessTokenValid() && jwtUtils.isRefreshTokenValid() -> {
                // update access token with refresh token and log in
                requestAccessTokenUpdate()
                _mainState.update { currentState ->
                    currentState.copy(authStatus = AuthStatus.LOGGED_IN)
                }
            }

            else -> {
                // show login screen
                _mainState.update { currentState ->
                    currentState.copy(authStatus = AuthStatus.LOGGED_OUT)
                }
            }
        }
    }

    private suspend fun requestAccessTokenUpdate() {
        dataStoreRepository.readFromDataStore(refreshToken)?.let { refreshToken ->
            usersInteractor.refresh(
                refreshToken = refreshToken
            ).onSuccess { newToken ->
                dataStoreRepository.writeToDataStore(accessToken, newToken.accessToken)
            }.onFailure {

            }
        }
    }

    fun refresh(needRefresh: Boolean) {
        _mainState.update { currentState ->
            currentState.copy(needRefresh = needRefresh)
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStoreRepository.writeToDataStore(accessToken, "")
            dataStoreRepository.writeToDataStore(refreshToken, "")
            dataStoreRepository.writeToDataStore(userId, "")
            _mainState.update { currentState ->
                currentState.copy(needRefresh = true, authStatus = AuthStatus.LOGGED_OUT)
            }
        }
    }

    enum class AuthStatus {
        LOGGED_IN, LOGGED_OUT, PROCESSING
    }

    data class MainState(
        val authStatus: AuthStatus = AuthStatus.PROCESSING,
        val needRefresh: Boolean = true,
    )
}