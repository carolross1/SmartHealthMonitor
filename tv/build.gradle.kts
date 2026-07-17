import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

// Credenciales MQTT leidas desde local.properties (NO se suben al repo)
val localProperties = Properties().apply {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) load(FileInputStream(localFile))
}

android {
    namespace = "mx.utng.smarthealthmonitor.tv"
    compileSdk = 35

    defaultConfig {
        applicationId = "mx.utng.smarthealthmonitor.tv"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "HIVEMQ_BROKER_URL", "\"${localProperties.getProperty("HIVEMQ_BROKER_URL", "ssl://TU-CLUSTER.hivemq.cloud:8883")}\"")
        buildConfigField("String", "HIVEMQ_USERNAME", "\"${localProperties.getProperty("HIVEMQ_USERNAME", "")}\"")
        buildConfigField("String", "HIVEMQ_PASSWORD", "\"${localProperties.getProperty("HIVEMQ_PASSWORD", "")}\"")
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
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // BOM más reciente que el resto del proyecto: TvLazyColumn/TvLazyRow ya NO
    // existen en tv-foundation:1.0.0 estable, así que usamos LazyColumn/LazyRow
    // normales de Compose Foundation (soportan foco D-pad nativo desde 1.7.0+).
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation")
    // androidx.tv.material3 no incluye CircularProgressIndicator (hueco conocido
    // de la API TV, ver issuetracker.google.com/issues/316676796) — se usa el de
    // Compose Material3 normal para ese componente.
    implementation("androidx.compose.material3:material3")

    // Compose for TV — solo para Surface/Text/MaterialTheme con foco D-pad
    implementation("androidx.tv:tv-foundation:1.0.0")
    implementation("androidx.tv:tv-material:1.0.0")

    // Navigation + ViewModel Compose (mismo patrón que app/wear)
    implementation("androidx.navigation:navigation-compose:2.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

    // Media3 ExoPlayer — reproductor de video (Sesión 12)
    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")

    // Room DB (entidad y DAO propios del módulo tv, igual que en wear,
    // para evitar la dependencia cruzada app<->tv que ya dio problemas
    // de AAPT en el módulo wear — ver commit f5e618f)
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Eclipse Paho MQTT (Sesión 13 - HiveMQ Cloud)
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1")
    // Kotlinx Serialization para JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    testImplementation(libs.junit)
    debugImplementation("androidx.compose.ui:ui-tooling")
}
