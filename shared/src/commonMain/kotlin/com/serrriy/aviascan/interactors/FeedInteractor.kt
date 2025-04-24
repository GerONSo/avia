package com.serrriy.aviascan.interactors

import com.serrriy.aviascan.data.feed.FeedListResponse
import com.serrriy.aviascan.data.posts.PostPublishRequest
import com.serrriy.aviascan.getClient
import com.serrriy.aviascan.utils.TokenProvider
import com.serrriy.aviascan.utils.safeCall
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path

class FeedInteractor(
    private val tokenProvider: TokenProvider
) {
    suspend fun getFeed(userId: String): Result<FeedListResponse> {
        return safeCall {
            getClient(tokenProvider).get {
                url {
                    path("/posts/feed/$userId")
                }
            }
        }
    }

    suspend fun getOwnFeed(userId: String): Result<FeedListResponse> {
        return safeCall {
            getClient(tokenProvider).get {
                url {
                    path("/posts/list/$userId")
                }
            }
        }
    }

    suspend fun publish(
        title: String,
        id: String,
    ): Result<Any?> {
        return safeCall {
            getClient(tokenProvider).post {
                url {
                    path("/posts/publish")
                }
                setBody(
                    PostPublishRequest(
                        id = id,
                        title = title,
                    )
                )
            }
        }
    }
}