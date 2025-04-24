package com.serrriy.aviascan.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serrriy.aviascan.data.feed.FeedListItemDto
import com.serrriy.aviascan.feed.data.FeedListItem
import com.serrriy.aviascan.feed.data.OwnFeedListItem
import com.serrriy.aviascan.feed.data.toFeedListItem
import com.serrriy.aviascan.feed.data.toOwnFeedListItem
import com.serrriy.aviascan.interactors.FeedInteractor
import com.serrriy.aviascan.repositories.DataStoreRepository
import com.serrriy.aviascan.utils.TokenProviderImpl
import com.serrriy.aviascan.utils.withUserIdFrom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Collections

class FeedViewModel(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private val feedInteractor = FeedInteractor(
        tokenProvider = TokenProviderImpl(dataStoreRepository = dataStoreRepository)
    )

    private val _uiState = MutableStateFlow(FeedState())
    val uiState: StateFlow<FeedState> = _uiState.asStateFlow()

    fun getFeed() {
        _uiState.update { currentState ->
            currentState.copy(refreshing = true)
        }
        viewModelScope.launch {
            withUserIdFrom(dataStoreRepository) { userId ->
                feedInteractor.getFeed(userId).onSuccess { result ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            postsList = result.posts.map(FeedListItemDto::toFeedListItem),
                            refreshing = false
                        )
                    }
                }.onFailure {
                    _uiState.update { currentState ->
                        currentState.copy(
                            refreshing = false
                        )
                    }
                }

                feedInteractor.getOwnFeed(userId).onSuccess { result ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            ownPostList = result.posts.map(FeedListItemDto::toOwnFeedListItem),
                            refreshing = false
                        )
                    }
                }.onFailure {
                    _uiState.update { currentState ->
                        currentState.copy(
                            refreshing = false
                        )
                    }
                }
            }
        }

    }

    fun changeTab(tab: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedTab = tab,
            )
        }
    }

    fun titleChanged(position: Int, title: String) {
        _uiState.update { currentState ->
            currentState.copy(
                ownPostList = currentState.ownPostList.toMutableList().apply {
                    set(
                        index = position,
                        element = this[position].copy(
                            post = this[position].post.copy(
                                title = title
                            )
                        )
                    )
                }
            )
        }
    }

    fun publish(index: Int) {
        viewModelScope.launch {
            val postId = uiState.value.ownPostList[index].post.id
            val title = uiState.value.ownPostList[index].post.title ?: return@launch
            feedInteractor.publish(
                id = postId,
                title = title
            ).onSuccess {

            }.onFailure {

            }
        }
    }
}

data class FeedState(
    val postsList: List<FeedListItem> = Collections.emptyList(),
    val ownPostList: List<OwnFeedListItem> = Collections.emptyList(),
    val selectedTab: Int = 0,
    val refreshing: Boolean = false,
)