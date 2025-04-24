package com.serrriy.aviascan.utils

import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.serialization.SerializationException
import kotlin.Result

sealed class ApiException(
    override val message: String,
    open val code: Int? = null
) : Exception(message) {

    data class BadRequest(
        override val message: String,
        override val code: Int = HttpStatusCode.BadRequest.value,
    ) : ApiException(message, code)

    data class Unauthorized(
        override val message: String = "Unauthorized",
        override val code: Int = HttpStatusCode.Unauthorized.value
    ) : ApiException(message, code)

    data class ServerError(
        override val message: String,
        override val code: Int
    ) : ApiException(message, code)

    data class NetworkError(
        override val message: String
    ) : ApiException(message)

    data class SerializationError(
        override val message: String
    ) : ApiException(message)
}

suspend inline fun <reified T> safeCall(call: () -> HttpResponse): Result<T> {
    return try {
        val response = call.invoke()
        if (response.status.isSuccess()) {
            Result.success(response.body())
        } else {
            Result.failure(handleErrorResponse(response))
        }
    } catch (e: Exception) {
        Result.failure(handleNetworkException(e))
    }
}


fun handleErrorResponse(response: HttpResponse): ApiException {
    return when (response.status) {
        HttpStatusCode.BadRequest -> ApiException.BadRequest(
            message = "Invalid request",
            code = response.status.value,
        )
        HttpStatusCode.Unauthorized -> ApiException.Unauthorized(
            message = "Authentication required",
            code = response.status.value
        )
        else -> ApiException.ServerError(
            message = "Server error occurred",
            code = response.status.value
        )
    }
}

fun handleNetworkException(e: Exception): ApiException {
    return when (e) {
        is ClientRequestException -> ApiException.BadRequest(
            message = "Client error: ${e.response.status.description}",
            code = e.response.status.value
        )
        is ServerResponseException -> ApiException.ServerError(
            message = "Server error: ${e.response.status.description}",
            code = e.response.status.value
        )
        is RedirectResponseException -> ApiException.ServerError(
            message = "Redirect error: ${e.response.status.description}",
            code = e.response.status.value
        )
        is SerializationException -> ApiException.SerializationError(
            message = "Serialization error: ${e.message}"
        )
        else -> ApiException.NetworkError(
            message = "Network error: ${e.message ?: "Unknown"}"
        )
    }
}

