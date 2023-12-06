plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
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
    // LottieFiles -> Animation management by JSON
    //implementation("com.airbnb-android:lottie:6.1.0")

    // Google Material Icons
    implementation("com.google.android.material:material:1.10.0")

    // Observe As State and LiveData management
    implementation("androidx.compose.runtime:runtime-livedata:1.6.0-beta01")

    // Dependency injection
    implementation("com.google.dagger:hilt-android:2.46.1")
    implementation("com.google.dagger:hilt-android-compiler:2.45")

    // Text animations -> Accompanist
    implementation("com.google.accompanist:accompanist-pager:0.28.0")

    // Loc -> Location services
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Lottie -> Animation Files
    implementation("com.airbnb.android:lottie-compose:6.1.0")
}

class Versions {
    companion object {
        const val COMPOSE_VERSION = "1.5.4"
        const val COMPOSE_NAV_VERSION = "2.7.5"
        const val COROUTINES_VERSION = "1.7.1"
        const val COMPOSE_BOM_VERSION = "2023.03.00"
    }
}