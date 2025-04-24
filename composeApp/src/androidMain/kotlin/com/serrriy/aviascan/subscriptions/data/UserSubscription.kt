package com.serrriy.aviascan.subscriptions.data

import com.serrriy.aviascan.data.user.UserResponse

data class UserSubscription(
    val id: String,
    val name: String,
    val isSubscribed: Boolean,
)

fun UserResponse.toUserSubscription(isSubscribed: Boolean) = UserSubscription(
    id = id,
    name = name,
    isSubscribed = isSubscribed,
)