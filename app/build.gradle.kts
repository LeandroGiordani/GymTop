plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    // Processes google-services.json and configures Firebase at build time
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.example.gymtop"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.gymtop"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    // Extended icons (Visibility, VisibilityOff, etc.) — not bundled in the core icon set
    implementation(libs.androidx.compose.material.icons.extended)

    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // App Startup — initializes libraries at app launch
    implementation(libs.androidx.startup.runtime)
    
    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    
    // Navigation Compose
    implementation(libs.androidx.navigation.compose)
    
    // ViewModel Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    
    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    // .await() extension for Firebase Tasks — bridges Firebase callbacks to coroutines
    implementation(libs.kotlinx.coroutines.play.services)

    // Hilt Navigation Compose
    implementation(libs.androidx.hilt.navigation.compose)

    // Lifecycle Runtime Compose (collectAsStateWithLifecycle)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // ── Firebase ──────────────────────────────────────────────────────────────
    // BOM controls all Firebase versions — no need to pin each one individually
    implementation(platform(libs.firebase.bom))
    // Firebase Authentication (email/password + Google Sign-In credential)
    implementation(libs.firebase.auth)
    // Cloud Firestore — stores user profile (name, gender) beyond what Auth provides
    implementation(libs.firebase.firestore)
    // Google Sign-In — provides the ID token passed to Firebase Auth
    implementation(libs.play.services.auth)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
