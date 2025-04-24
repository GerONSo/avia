package com.serrriy.aviascan.interactors

import com.serrriy.aviascan.data.user.ChangeUserInfoRequest
import com.serrriy.aviascan.data.user.CreateUserRequest
import com.serrriy.aviascan.data.user.GetUserRequest
import com.serrriy.aviascan.data.user.TokenRefreshRequest
import com.serrriy.aviascan.data.user.TokenResponse
import com.serrriy.aviascan.data.user.UserResponse
import com.serrriy.aviascan.data.user.UserWithTokenResponse
import com.serrriy.aviascan.getClient
import com.serrriy.aviascan.utils.TokenProvider
import com.serrriy.aviascan.utils.safeCall
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path

class UsersInteractor(
    private val tokenProvider: TokenProvider
) {
    suspend fun login(
        email: String,
        password: String,
    ): Result<UserWithTokenResponse> {
        return safeCall {
            getClient(tokenProvider).post {
                url {
                    path("/users/login")
                }
                setBody(
                    GetUserRequest(
                        email = email,
                        password = password,
                    )
                )
            }
        }
    }

    suspend fun register(
        email: String,
        name: String,
        password: String,
    ): Result<UserWithTokenResponse> {
        return safeCall {
            getClient(tokenProvider).post {
                url {
                    path("/users/create")
                }
                setBody(
                    CreateUserRequest(
                        name = name,
                        email = email,
                        password = password,
                    )
                )
            }
        }
    }

    suspend fun refresh(
        refreshToken: String,
    ): Result<TokenResponse> {
        return safeCall {
            getClient(tokenProvider).post {
                url {
                    path("token/refresh")
                }
                setBody(TokenRefreshRequest(refreshToken))
            }
        }
    }

    suspend fun profile(
        userId: String,
    ): Result<UserResponse> {
        return safeCall {
            getClient(tokenProvider).get {
                url {
                    path("users/profile/$userId")
                }
            }
        }
    }

    suspend fun changeUserInfo(
        userId: String,
        newName: String,
    ): Result<Any?> {
        return safeCall {
            getClient(tokenProvider).post {
                url {
                    path("users/changeUserInfo")
                }
                setBody(
                    ChangeUserInfoRequest(
                        userId = userId,
                        name = newName,
                    )
                )
            }
        }
    }
}