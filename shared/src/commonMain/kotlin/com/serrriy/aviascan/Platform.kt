package com.serrriy.aviascan

import io.ktor.client.HttpClient

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect val client: HttpClient