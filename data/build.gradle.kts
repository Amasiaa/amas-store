plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "mg.amas.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
}

dependencies {

    implementation(project(":domain"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    // api(libs.koin.core) // koin
    implementation(libs.ktor.client.android) // ktor client android
    implementation(libs.ktor.client.core) // ktor client core
    implementation(libs.ktor.client.cio) // ktor client cio
    implementation(libs.ktor.client.logging) // ktor client logging
    implementation(libs.ktor.client.content.negotiation) // ktor client content negotiation
    implementation(libs.ktor.serialization.kotlinx.json) // ktor serialization kotlinx json
    implementation(libs.ktor.utils) // ktor utils
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
