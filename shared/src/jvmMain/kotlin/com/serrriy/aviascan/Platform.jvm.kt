package com.serrriy.aviascan

import com.serrriy.aviascan.utils.TokenProvider
import io.ktor.client.HttpClient

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()
actual fun getClient(tokenProvider: TokenProvider): HttpClient {
    TODO("Not yet implemented")
}

actual val aviationStackClient: HttpClient
    get() = TODO()