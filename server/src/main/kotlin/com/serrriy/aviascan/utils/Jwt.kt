package com.serrriy.aviascan.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JwtConfig {
    private const val secret = "test-secret-key"
    private const val issuer = "AviaScan"
    private const val accessTokenValidity = 15 * 60 * 1000 // 15 mins
    private const val refreshTokenValidity = 30L * 24 * 60 * 60 * 1000 // 30 days

    private val algorithm = Algorithm.HMAC256(secret)

    fun generateAccessToken(userId: String): String =
        JWT.create()
            .withIssuer(issuer)
            .withSubject(userId)
            .withClaim("type", "access")
            .withExpiresAt(Date(System.currentTimeMillis() + accessTokenValidity))
            .sign(algorithm)

    fun generateRefreshToken(userId: String): String =
        JWT.create()
            .withIssuer(issuer)
            .withSubject(userId)
            .withClaim("type", "refresh")
            .withExpiresAt(Date(System.currentTimeMillis() + refreshTokenValidity))
            .sign(algorithm)

    fun getVerifier() = JWT.require(algorithm)
        .withIssuer(issuer)
        .build()
}
