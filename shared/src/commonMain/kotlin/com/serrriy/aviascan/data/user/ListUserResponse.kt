package com.serrriy.aviascan.data.user

import com.serrriy.aviascan.data.user.UserResponse
import kotlinx.serialization.Serializable


@Serializable
data class ListUserResponse(
    val subscribedUsers: List<UserResponse>?, 
    val users: List<UserResponse>?,
    val startFrom: String?,
)
