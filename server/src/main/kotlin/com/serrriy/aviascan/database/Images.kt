package com.serrriy.aviascan.database

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.S3Exception
import java.net.URI
import java.util.*

class ImageRepository(
    accessKey: String,
    secretKey: String,
    region: String,
    private val endpointUrl: String,
    private val bucket: String
) {
    private val s3Client: S3Client = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            )
        )
        .endpointOverride(URI.create(endpointUrl))
        .forcePathStyle(true)
        .build()

    fun uploadImage(imageBytes: ByteArray, originalFileName: String, basePath: String): String {
        val key = "$basePath/${UUID.randomUUID()}-$originalFileName"

        val request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build()

        try {
            s3Client.putObject(request, RequestBody.fromBytes(imageBytes))
        } catch (e: S3Exception) {
            throw RuntimeException("Failed to upload image to S3: ${e.message}", e)
        }

        return "$endpointUrl/$bucket/$key"
    }
}
