plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "mx.utng.smarthealthmonitor.wear"
    compileSdk = 35

    defaultConfig {
        applicationId = "mx.utng.smarthealthmonitor"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)

    // Wear OS Compose
    implementation("androidx.wear.compose:compose-material:1.4.0")
    implementation("androidx.wear.compose:compose-foundation:1.4.0")

    // Health Services API (sensor FC real)
    implementation("androidx.health:health-services-client:1.1.0-alpha03")

    // Guava + Coroutines (para ListenableFuture.await())
    implementation("com.google.guava:guava:33.0.0-android")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-guava:1.7.3")

    // Wearable Data Layer (MessageClient para enviar al teléfono)
    implementation("com.google.android.gms:play-services-wearable:18.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // Lifecycle ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
}
