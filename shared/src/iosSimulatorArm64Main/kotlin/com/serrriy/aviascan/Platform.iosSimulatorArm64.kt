package com.serrriy.aviascan

import com.serrriy.aviascan.utils.TokenProvider
import io.ktor.client.HttpClient

actual fun getClient(tokenProvider: TokenProvider): HttpClient {
    TODO("Not yet implemented")
}

actual fun getPlatform(): Platform {
    TODO("Not yet implemented")
}

actual val aviationStackClient: HttpClient
    get() = TODO()