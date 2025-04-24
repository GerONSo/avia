package com.serrriy.aviascan.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.serrriy.aviascan.R
import com.serrriy.aviascan.feed.data.FeedListItem
import com.serrriy.aviascan.flights.FullItemInfo
import com.serrriy.aviascan.flights.data.toFlightListItem
import kotlinx.datetime.toLocalDateTime

@Composable
fun FeedItem(feedItem: FeedListItem) {
    val fallbackIcon = rememberVectorPainter(Icons.Rounded.AccountCircle)
    Card(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(feedItem.profile.email)
                        .crossfade(true)
                        .build(),
                    error = fallbackIcon,
                    contentDescription = "User avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.width(50.dp).height(50.dp).clip(CircleShape),
                )
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(
                        text = feedItem.profile.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "${feedItem.post.createdAt.toLocalDateTime().date.dayOfMonth} ${feedItem.post.createdAt.toLocalDateTime().date.month}",
                        color = colorResource(R.color.Secondary)
                    )
                }
            }
            feedItem.post.title?.let { title ->
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            Column(modifier = Modifier.padding(top = 16.dp)) {
                FullItemInfo(
                    item = feedItem.flight.toFlightListItem()
                )
            }
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(feedItem.post.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Map snapshot",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp).clip(
                    RoundedCornerShape(16.dp)
                ),
            )
        }
    }
}