package com.serrriy.aviascan.routes

import io.ktor.http.content.*
import io.ktor.server.request.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.*
import java.awt.Image 

data class ImageUpload(
    val filename: String,
    val bytes: ByteArray
)

inline fun <reified T : Any> jsonDeserializer(): KSerializer<T> {
    return serializer()
}

suspend inline fun <reified T : Any> decodeMultipartForm(
    multipart: MultiPartData,
    jsonPartName: String = "metadata",
    imagePartName: String = "image",
): Pair<T, ImageUpload?> {
    var parsedJson: T? = null
    var imageUpload: ImageUpload? = null

    multipart.forEachPart { part ->
        when (part) {
            is PartData.FormItem -> {
                if (part.name == jsonPartName) {
                    parsedJson = Json.decodeFromString(jsonDeserializer(), part.value)
                }
            }
            is PartData.FileItem -> {
                if (part.name == imagePartName && part.originalFileName != null) {
                    imageUpload = ImageUpload(part.originalFileName!!, part.streamProvider().readBytes())
                }
            }
            else -> Unit
        }
        part.dispose()
    }

    if (parsedJson == null) {
        throw IllegalArgumentException("Missing required parts: $jsonPartName")
    }

    return Pair(parsedJson,  imageUpload)
}