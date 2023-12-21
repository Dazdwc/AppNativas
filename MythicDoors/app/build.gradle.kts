plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "org.helios.mythicdoors"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.helios.mythicdoors"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/gradle/incremental.annotation.processors"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:${Versions.COMPOSE_BOM_VERSION}"))
    implementation("androidx.compose.ui:ui:${Versions.COMPOSE_VERSION}")
    implementation("androidx.compose.ui:ui-graphics:${Versions.COMPOSE_VERSION}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.COMPOSE_VERSION}")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("io.coil-kt:coil-compose:2.2.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:${Versions.COMPOSE_BOM_VERSION}"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Versions.COMPOSE_VERSION}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.COMPOSE_VERSION}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${Versions.COMPOSE_VERSION}")

    // Most recent Kotlin Compiler Extension for Compose
    implementation("androidx.compose.compiler:compiler:${Versions.COMPOSE_VERSION}")

    // Compose navigation
    implementation("androidx.navigation:navigation-compose:${Versions.COMPOSE_NAV_VERSION}")
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.COMPOSE_NAV_VERSION}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.COMPOSE_NAV_VERSION}")

    // Coroutines -> Threading management
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES_VERSION}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES_VERSION}")

    // SQLite Helper -> Database implementation and management
    implementation("androidx.sqlite:sqlite-ktx:2.4.0")

    // Google Material Icons
    implementation("com.google.android.material:material:1.10.0")

    // Observe As State and LiveData management
    implementation("androidx.compose.runtime:runtime-livedata:1.6.0-beta02")

    // Dagger Hilt -> Dependency injection
    implementation("com.google.dagger:hilt-android:2.46.1")
    implementation("com.google.dagger:hilt-android-compiler:2.45")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    //kapt("com.google.dagger:hilt-android-compiler:2.46.1")

    // Text animations -> Accompanist
    implementation("com.google.accompanist:accompanist-pager:0.28.0")

    // Loc -> Location services
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // LottieFiles -> Animation management by JSON
    implementation("com.airbnb.android:lottie-compose:6.1.0")

    // Firebase -> Cloud services from Google
    // Play Services Base
    implementation("com.google.android.gms:play-services-base:18.2.0")
    // Coroutines for Firebase -> Threading management
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.1")
    // -> BoM: With BoM (Bill of Materials) we can manage all the Firebase dependencies in one place and keep them up to date
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    // -> Core
    implementation("com.google.firebase:firebase-core:21.1.1")
    //App Check -> Security
    implementation("com.google.firebase:firebase-appcheck-ktx:${Versions.FIREBASE_APP_CHECK}")
    implementation("com.google.firebase:firebase-appcheck-playintegrity:${Versions.FIREBASE_APP_CHECK}")
    implementation("com.google.firebase:firebase-appcheck-debug:${Versions.FIREBASE_APP_CHECK}")
    // -> Auth
    implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    // -> Analytics
    implementation("com.google.firebase:firebase-analytics-ktx:21.5.0")
    implementation("com.google.firebase:firebase-crashlytics-ktx:${Versions.FIREBASE_CRASH_ANALYTICS}")
    // Crashlytics -> Native crash reporting
    implementation("com.google.firebase:firebase-crashlytics-ktx:${Versions.FIREBASE_CRASH_ANALYTICS}")
    // -> Cloud Messaging
    implementation("com.google.firebase:firebase-messaging-ktx:23.4.0")
    // -> Database: Realtime Database
    implementation("com.google.firebase:firebase-database-ktx:${Versions.FIREBASE_DATABASE}")
    // -> Database: Storage
    implementation("com.google.firebase:firebase-storage-ktx:${Versions.FIREBASE_DATABASE}")
    // -> Database: Firestore
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.0")
    // -> In-App Messaging
    implementation("com.google.firebase:firebase-inappmessaging-display-ktx:20.4.0")
    // -> Remote Config
    implementation("com.google.firebase:firebase-config-ktx:21.6.0")
    // -> Performance Monitoring
    implementation("com.google.firebase:firebase-perf-ktx:20.5.1")

    // Moshi -> JSON management
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")

    // Retrofit -> Type-safe HTTP client requests management
    implementation("com.squareup.retrofit2:retrofit:${Versions.RETROFIT}")
    // Moshi converter -> JSON converter for Retrofit
    implementation("com.squareup.retrofit2:converter-moshi:${Versions.RETROFIT}")

    // Kotlin Deserialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    // Android Encryption -> Data encryption with security key
    implementation("androidx.security:security-crypto-ktx:1.1.0-alpha06")

    // Kotlin Flow Playground -> Flow management for collectAsStateWithLifecycle function
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

}

class Versions {
    companion object {
        const val COMPOSE_VERSION = "1.5.4"
        const val COMPOSE_NAV_VERSION = "2.7.5"
        const val COROUTINES_VERSION = "1.7.1"
        const val COMPOSE_BOM_VERSION = "2023.03.00"
        const val FIREBASE_APP_CHECK = "17.1.1"
        const val FIREBASE_CRASH_ANALYTICS = "18.6.0"
        const val FIREBASE_DATABASE = "20.3.0"
        const val RETROFIT = "2.9.0"
    }
}