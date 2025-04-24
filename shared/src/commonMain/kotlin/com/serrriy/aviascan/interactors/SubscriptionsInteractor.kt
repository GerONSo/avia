package com.serrriy.aviascan.interactors

import com.serrriy.aviascan.data.user.ListUserResponse
import com.serrriy.aviascan.data.user.SubscribeRequest
import com.serrriy.aviascan.getClient
import com.serrriy.aviascan.utils.TokenProvider
import com.serrriy.aviascan.utils.safeCall
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path

class SubscriptionsInteractor(
    private val tokenProvider: TokenProvider
) {
    suspend fun getSubscriptions(userId: String): Result<ListUserResponse> {
        return safeCall {
            getClient(tokenProvider).get {
                url {
                    path("/users/list/$userId")
                }
            }
        }
    }

    suspend fun subscribe(ownUserId: String, userId: String): Result<Any?> {
        return safeCall {
            getClient(tokenProvider).post {
                url {
                    path("/users/subscribe")
                }
                setBody(SubscribeRequest(userId = ownUserId, subscribeTo = userId))
            }
        }
    }

    suspend fun unsubscribe(ownUserId: String, userId: String): Result<Any?> {
        return safeCall {
            getClient(tokenProvider).post {
                url {
                    path("/users/unsubscribe")
                }
                setBody(SubscribeRequest(userId = ownUserId, subscribeTo = userId))
            }
        }
    }
}