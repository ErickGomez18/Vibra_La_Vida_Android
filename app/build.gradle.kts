plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.vibralavida"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.vibralavida"

        // Necesario para usar Health Connect connect-client:1.1.0.
        minSdk = 26

        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))

    // ============================================================================
// RETROFIT
// ============================================================================

    implementation("com.squareup.retrofit2:retrofit:2.11.0")


    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // ========================================================================
    // FIREBASE
    // ========================================================================

    implementation(platform("com.google.firebase:firebase-bom:34.0.0"))

    implementation("com.google.firebase:firebase-auth")

    implementation("com.google.firebase:firebase-firestore")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.3")

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    implementation("io.coil-kt:coil-compose:2.7.0")

    // Health Connect estable.
    implementation("androidx.health.connect:connect-client:1.1.0")

    // Permite usar java.time en versiones compatibles.
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")

    debugImplementation("androidx.compose.ui:ui-tooling")
}

