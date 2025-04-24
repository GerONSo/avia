package com.serrriy.aviascan.profile.achievements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serrriy.aviascan.profile.ProfileViewModel

@Composable
fun AchievementsScreen(
    profileViewModel: ProfileViewModel
) {
    val uiState by profileViewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        profileViewModel.getAchievements()
    }

    Column {
        Text(
            text = "Your achievements",
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            modifier = Modifier.padding(top = 26.dp, start = 16.dp)
        )

        LazyRow (
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 36.dp, start = 16.dp, end = 16.dp)
        ) {
            itemsIndexed(uiState.achievementsList) { index, item ->
                AchievementItem(item)
            }
        }
    }
}