package com.serrriy.aviascan.subscriptions

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.serrriy.aviascan.main.EmptyList
import com.serrriy.aviascan.repositories.DataStoreRepository


@Composable
fun rememberSubscriptionsViewModel(): SubscriptionViewModel {
    val context = LocalContext.current
    val factory = remember {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SubscriptionViewModel(dataStoreRepository = DataStoreRepository(context)) as T
            }
        }
    }
    return viewModel(factory = factory)
}
@Composable
fun SubscriptionsScreen(
    viewModel: SubscriptionViewModel = rememberSubscriptionsViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getSubscriptions()
    }

    LazyColumn(
        Modifier.fillMaxWidth().fillMaxHeight()
            .padding(top = 26.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        item {
            Text(
                text = "Subscriptions",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        if (!uiState.subscribedUsers.isNullOrEmpty()) {
            itemsIndexed(uiState.subscribedUsers!!) { _, user ->
                SubscriptionItem(
                    userSubscription = user,
                    onSubscribeClicked = { },
                    onUnsubscribeClicked = { user ->
                        viewModel.unsubscribe(user = user)
                    }
                )
            }
        } else {
            item {
                EmptyList()
            }
        }

        item {
            Text(
                text = "All users",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 26.dp, bottom = 8.dp)
            )
        }

        if (!uiState.users.isNullOrEmpty()) {
            itemsIndexed(uiState.users!!) { _, user ->
                SubscriptionItem(
                    userSubscription = user,
                    onSubscribeClicked = { user ->
                        viewModel.subscribeTo(user = user)
                    },
                    onUnsubscribeClicked = { }
                )
            }
        } else {
            item {
                EmptyList()
            }
        }
    }
}