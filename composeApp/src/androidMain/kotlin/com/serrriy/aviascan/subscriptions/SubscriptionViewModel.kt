package com.serrriy.aviascan.subscriptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serrriy.aviascan.interactors.SubscriptionsInteractor
import com.serrriy.aviascan.repositories.DataStoreKeys
import com.serrriy.aviascan.repositories.DataStoreRepository
import com.serrriy.aviascan.subscriptions.data.UserSubscription
import com.serrriy.aviascan.subscriptions.data.toUserSubscription
import com.serrriy.aviascan.utils.TokenProviderImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SubscriptionViewModel(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private val subscriptionsInteractor = SubscriptionsInteractor(
        tokenProvider = TokenProviderImpl(dataStoreRepository = dataStoreRepository)
    )

    private val _uiState = MutableStateFlow(SubscriptionsState())
    val uiState: StateFlow<SubscriptionsState> = _uiState.asStateFlow()

    fun getSubscriptions() {
        viewModelScope.launch {
            dataStoreRepository.readFromDataStore(DataStoreKeys.userId)?.let { userId ->
                subscriptionsInteractor.getSubscriptions(
                    userId = userId
                ).onSuccess { result ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            subscribedUsers = result.subscribedUsers?.map {
                                it.toUserSubscription(isSubscribed = true)
                            },
                            users = result.users?.map {
                                it.toUserSubscription(isSubscribed = false)
                            },
                        )
                    }
                }.onFailure {

                }
            }
        }
    }

    fun subscribeTo(user: UserSubscription) {
        viewModelScope.launch {
            dataStoreRepository.readFromDataStore(DataStoreKeys.userId)?.let { ownUserId ->
                subscriptionsInteractor.subscribe(ownUserId, user.id)
                    .onSuccess {
                        _uiState.update { currentState ->
                            currentState.copy(
                                subscribedUsers = currentState.subscribedUsers?.let {
                                    it + user.copy(isSubscribed = true)
                                },
                                users = currentState.users?.filter {
                                    it.id != user.id
                                },
                            )
                        }
                    }.onFailure {

                    }
            }
        }
    }

    fun unsubscribe(user: UserSubscription) {
        viewModelScope.launch {
            dataStoreRepository.readFromDataStore(DataStoreKeys.userId)?.let { ownUserId ->
                subscriptionsInteractor.unsubscribe(ownUserId, user.id)
                    .onSuccess {
                        _uiState.update { currentState ->
                            currentState.copy(
                                subscribedUsers = currentState.subscribedUsers?.filter {
                                    it.id != user.id
                                },
                                users = currentState.users?.let {
                                    it + user.copy(isSubscribed = false)
                                },
                            )
                        }
                    }.onFailure {

                    }
            }
        }
    }
}

data class SubscriptionsState(
    val subscribedUsers: List<UserSubscription>? = null,
    val users: List<UserSubscription>? = null,
)