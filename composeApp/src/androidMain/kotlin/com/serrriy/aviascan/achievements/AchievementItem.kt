package com.serrriy.aviascan.achievements

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.serrriy.aviascan.achievements.data.AchievementListItem

@Composable
fun AchievementItem(
    achievementListItem: AchievementListItem
) {
    Column(modifier = Modifier) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(achievementListItem.image)
                .crossfade(true)
                .build(),
            contentDescription = "Achievement",
            contentScale = ContentScale.Crop,
            modifier = Modifier.width(100.dp).height(100.dp).align(Alignment.CenterHorizontally),
        )

        Text(
            text = achievementListItem.title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp).align(Alignment.CenterHorizontally)
        )
    }
}