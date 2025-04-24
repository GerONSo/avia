package com.serrriy.aviascan.data.posts

import kotlinx.serialization.Serializable
import com.serrriy.aviascan.data.posts.PostDto

@Serializable
data class PostListResponse(
    val posts: List<PostDto>
)
