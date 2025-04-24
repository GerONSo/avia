package com.serrriy.aviascan.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.serrriy.aviascan.feed.data.FeedListItem
import com.serrriy.aviascan.main.EmptyList
import com.serrriy.aviascan.repositories.DataStoreRepository
import kotlinx.coroutines.launch


@Composable
fun rememberFeedViewModel(): FeedViewModel {
    val context = LocalContext.current
    val factory = remember {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FeedViewModel(
                    dataStoreRepository = DataStoreRepository(context),
                ) as T
            }
        }
    }
    return viewModel(factory = factory)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    feedViewModel: FeedViewModel = rememberFeedViewModel()
) {
    val uiState by feedViewModel.uiState.collectAsState()
    val tabs = listOf("Shared", "Own")
    val refreshScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        feedViewModel.getFeed()
    }


    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = uiState.selectedTab,
            modifier = Modifier.padding(8.dp),
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[uiState.selectedTab]),
                    color = Color.Black
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = uiState.selectedTab == index,
                    onClick = { feedViewModel.changeTab(index) },
                    text = { Text(text = title) },
                    selectedContentColor = Color.Black,
                    unselectedContentColor = Color.Gray
                )
            }
        }
        PullToRefreshBox(
            state = rememberPullToRefreshState(),
            onRefresh = {
                refreshScope.launch {
                    feedViewModel.getFeed()
                }
            },
            modifier = Modifier.fillMaxSize(),
            isRefreshing = uiState.refreshing
        ) {

            LazyColumn(
                Modifier.fillMaxWidth().fillMaxHeight()
                    .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                when (uiState.selectedTab) {
                    0 -> {
                        if (uiState.postsList.isNotEmpty()) {
                            itemsIndexed(uiState.postsList) { _, feedItem ->
                                FeedItem(feedItem)
                            }
                        } else {
                            item {
                                EmptyList()
                            }
                        }
                    }

                    1 -> {
                        if (uiState.ownPostList.isNotEmpty()) {
                            itemsIndexed(uiState.ownPostList) { index, feedItem ->
                                if (feedItem.post.isPublished) {
                                    FeedItem(
                                        FeedListItem(
                                            profile = feedItem.profile,
                                            post = feedItem.post,
                                            flight = feedItem.flight,
                                        )
                                    )
                                } else {
                                    OwnFeedPost(feedItem, uiState, feedViewModel, index)
                                }
                            }
                        } else {
                            item {
                                EmptyList()
                            }
                        }
                    }
                }
            }
        }
    }
}
