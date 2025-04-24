package com.serrriy.aviascan.data.user

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val imageUrl: String?,
)
