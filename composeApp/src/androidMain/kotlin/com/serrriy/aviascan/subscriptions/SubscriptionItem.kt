package com.serrriy.aviascan.subscriptions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serrriy.aviascan.R
import com.serrriy.aviascan.subscriptions.data.UserSubscription

@Composable
fun SubscriptionItem(
    userSubscription: UserSubscription,
    onSubscribeClicked: (user: UserSubscription) -> Unit,
    onUnsubscribeClicked: (user: UserSubscription) -> Unit,
) {
    Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = userSubscription.name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = colorResource(R.color.Secondary),
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        Spacer(Modifier.weight(1f))

        if (!userSubscription.isSubscribed) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add subscription",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable {
                        onSubscribeClicked(userSubscription)
                    }
            )
        } else {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete subscription",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable {
                        onUnsubscribeClicked(userSubscription)
                    }
            )
        }
    }

}