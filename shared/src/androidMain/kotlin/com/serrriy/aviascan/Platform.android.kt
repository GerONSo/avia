package com.serrriy.aviascan

import android.os.Build
import com.serrriy.aviascan.data.user.TokenRefreshRequest
import com.serrriy.aviascan.data.user.TokenResponse
import com.serrriy.aviascan.utils.TokenProvider
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun getClient(tokenProvider: TokenProvider): HttpClient {
    return HttpClient(Android) {
        install(HttpTimeout) {
            socketTimeoutMillis = 60_000
            requestTimeoutMillis = 60_000
        }
        engine {
            connectTimeout = 100_000
            socketTimeout = 100_000
        }

        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }

        defaultRequest {
            header("Content-Type", "application/json")
            url(URL)
        }

        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(tokenProvider.getAccessToken(), tokenProvider.getRefreshToken())
                }
                refreshTokens {
                    val newTokens: TokenResponse = getClient(tokenProvider).post {
                        url {
                            path("token/refresh")
                        }
                        setBody(TokenRefreshRequest(tokenProvider.getRefreshToken()))
                    }.body()
                    tokenProvider.setAccessToken(newTokens.accessToken)
                   BearerTokens(newTokens.accessToken, newTokens.refreshToken)
                }
            }
        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false
            })
        }
    }
}

actual val aviationStackClient = HttpClient(Android) {
    install(HttpTimeout) {
        socketTimeoutMillis = 60_000
        requestTimeoutMillis = 60_000
    }

    install(Logging) {
        logger = Logger.ANDROID
        level = LogLevel.ALL
    }

    defaultRequest {
//            header("Content-Type", "application/json")
//            url(URL)
    }

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            explicitNulls = false
        })
    }
}