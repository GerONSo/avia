package com.serrriy.aviascan

import com.serrriy.aviascan.utils.TokenProvider
import io.ktor.client.HttpClient

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun getClient(tokenProvider: TokenProvider): HttpClient

expect val aviationStackClient: HttpClient