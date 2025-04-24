
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.datetime)
            implementation(libs.kotlinx.serialization.json) // KotlinX Serialization (Convert JSON response to Kotlin Objects)
            implementation(libs.ktor.serialization.kotlinx.json) // Ktor- To work with Serialization
            implementation(libs.ktor.client.logging) // Logging (Optional)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth)
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.android)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
//                implementation(libs.ktor.client.darwin)
            }
        }
    }
}

android {
    namespace = "com.serrriy.aviascan.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        val apiKeys: Map<String, String> by rootProject.extra
        buildConfigField("String", "MAPKIT_API_KEY", "\"${apiKeys["mapKit"] ?: ""}\"")
        buildConfigField("String", "AVIATIONSTACK_API_KEY", "\"${apiKeys["aviationStack"] ?: ""}\"")
    }
    buildFeatures {
        buildConfig = true
    }
}

