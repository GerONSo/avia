package com.serrriy.aviascan.feed

import androidx.lifecycle.ViewModel
import com.serrriy.aviascan.data.feed.FeedListItemDto
import com.serrriy.aviascan.feed.data.FeedListItem
import com.serrriy.aviascan.feed.data.OwnFeedListItem
import com.serrriy.aviascan.feed.data.toFeedListItem
import com.serrriy.aviascan.feed.data.toOwnFeedListItem
import com.serrriy.aviascan.interactors.FeedInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Collections

class FeedViewModel : ViewModel() {
    private val feedInteractor = FeedInteractor()

    private val _uiState = MutableStateFlow(FeedState())
    val uiState: StateFlow<FeedState> = _uiState.asStateFlow()

    init {
        getFeed()
    }

    fun getFeed() {
        _uiState.update { currentState ->
            currentState.copy(
                postsList = feedInteractor.getFeed().map(FeedListItemDto::toFeedListItem),
                ownPostList = feedInteractor.getOwnFeed().map(FeedListItemDto::toOwnFeedListItem)
            )
        }
    }

    fun changeTab(tab: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedTab = tab,
            )
        }
    }

    fun titleChanged(title: String) {
        _uiState.update { currentState ->
            currentState.copy(
                titleInput = title
            )
        }
    }
}

data class FeedState(
    val postsList: List<FeedListItem> = Collections.emptyList(),
    val ownPostList: List<OwnFeedListItem> = Collections.emptyList(),
    val selectedTab: Int = 0,
    val titleInput: String = "",
)