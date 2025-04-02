package com.serrriy.aviascan.feed

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Shapes
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.serrriy.aviascan.R
import com.serrriy.aviascan.feed.data.FeedListItem
import com.serrriy.aviascan.flights.BottomSheetFlightListItem
import com.serrriy.aviascan.flights.BottomSheetViewModel
import com.serrriy.aviascan.flights.FullItemInfo
import com.serrriy.aviascan.flights.data.toFlightListItem
import com.serrriy.aviascan.flights.toSimpleDate
import com.serrriy.aviascan.utils.toDateTime

@Composable
fun FeedScreen(
    feedViewModel: FeedViewModel = viewModel()
) {
    val uiState by feedViewModel.uiState.collectAsState()
    val tabs = listOf("Shared", "Own")

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

        LazyColumn(
            Modifier.fillMaxWidth().fillMaxHeight()
                .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            when (uiState.selectedTab) {
                0 -> {
                    itemsIndexed(uiState.postsList) { _, feedItem ->
                        FeedItem(feedItem)
                    }
                }

                1 -> {
                    itemsIndexed(uiState.ownPostList) { _, feedItem ->
                        OwnFeedPost(feedItem, uiState, feedViewModel)
                    }
                }
            }
        }
    }
}
