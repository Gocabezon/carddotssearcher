plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.carddotsearcher"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.carddotsearcher"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}



dependencies {

        // Dependencias de Compose BOM (debe ser la primera)
        implementation(platform(libs.androidx.compose.bom))

        // 1. Dependencias Base de Android y Compose
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.activity.compose)

        // 2. Dependencias de UI de Compose
        implementation(libs.androidx.compose.ui)
        implementation(libs.androidx.compose.ui.graphics)
        implementation(libs.androidx.compose.ui.tooling.preview)
        implementation(libs.androidx.compose.material3)

        // 3. Navigation
        implementation(libs.androidx.navigation.compose)

        // 4. Arquitectura (ViewModel y Compose)
        // Usar la version compose para ViewModel y la version KTX para el runtime
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
        // implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0") // Solo si usas LiveData, si usas StateFlow no es necesaria.

        // 5. Networking (Retrofit)
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        // Interceptor para debugging: ¡Esencial!
        implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

        // 6. Kotlin Coroutines (Versión 1.7.3 es correcta)
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

        // 7. Localización (Play Services Location)
        implementation(libs.play.services.location)
    implementation(libs.androidx.runtime.livedata)

    // 8. Testing (Mantenido)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(libs.androidx.compose.ui.test.junit4)
        debugImplementation(libs.androidx.compose.ui.tooling)
        debugImplementation(libs.androidx.compose.ui.test.manifest)
}
