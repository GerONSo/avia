import java.util.Properties

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    id("com.palantir.docker-run") version "0.35.0"
    kotlin("plugin.serialization") version "2.1.0" apply false
}

val apiKeys by extra(getMapkitApiKey())

fun getMapkitApiKey(): Map<String, String> {
    val properties = Properties()
    project.file("local.properties").inputStream().also {
        properties.load(it)
    }
    return mapOf(
        "mapKit" to properties.getProperty("MAPKIT_API_KEY", ""),
        "aviationStack" to properties.getProperty("AVIATIONSTACK_API_KEY", "")
    )
}

dockerRun {
    name = "aviascan_postgres"
    image = "postgres:15"
    ports("5432:5432")  // Expose PostgreSQL to host machine
    env( mutableMapOf(
        "POSTGRES_DB" to "aviascan",
        "POSTGRES_USER" to "aviascan_root",
        "POSTGRES_PASSWORD" to "super_secret_password"
    ))
    daemonize = true  // Run in the background
}
