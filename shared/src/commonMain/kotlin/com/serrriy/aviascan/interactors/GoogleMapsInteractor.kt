package com.serrriy.aviascan.interactors

import com.serrriy.aviascan.aviationStackClient
import com.serrriy.aviascan.utils.safeCall
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol
import io.ktor.http.path

class GoogleMapsInteractor {
    suspend fun getSnapshot(
        apiKey: String,
        mapType: String,
        size: String,
        path: String,
    ): Result<ByteArray> {
        return safeCall {
            aviationStackClient.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "maps.googleapis.com"
                    path("/maps/api/staticmap")
                    parameter("key", apiKey)
                    parameter("maptype", mapType)
                    parameter("size", size)
                    parameter("path", path)
                }
            }
        }
    }
}