package com.serrriy.aviascan

import io.ktor.client.HttpClient

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()
actual val client: HttpClient
    get() = TODO("Not yet implemented")