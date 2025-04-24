
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    android.sourceSets.all {
        java.srcDir("src/androidMain/java")
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.google.maps.compose)
            implementation(libs.androidx.graphics.shapes)
            implementation(libs.advanced.bottomsheet)
            implementation(libs.androidx.constraintlayout)
            implementation(libs.datetime)
            implementation(libs.androidx.icons.compose)
            implementation(libs.androidx.navigation)
            implementation(libs.coil.compose)
            implementation(libs.coil.network)
            implementation(libs.accompanist)
            implementation(libs.androidx.camera.camera2)
            implementation(libs.androidx.camera.lifecycle)
            implementation(libs.androidx.camera.view)
            implementation(libs.barcode.scanning)
            implementation(libs.java.jwt)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.jwtdecode)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(projects.shared)
        }
    }
}

android {
    namespace = "com.serrriy.aviascan"
    compileSdk = libs.versions.android.compileSdk.get().toInt()


    defaultConfig {
        applicationId = "com.serrriy.aviascan"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        val apiKeys: Map<String, String> by rootProject.extra
        manifestPlaceholders["MAPKIT_API_KEY"] = apiKeys["mapKit"] ?: ""
        manifestPlaceholders["AVIATIONSTACK_API_KEY"] = apiKeys["aviationStack"] ?: ""
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

