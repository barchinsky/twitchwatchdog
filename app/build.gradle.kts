import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    id("twitchwatchdog.android.application.compose")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.m.twitchwatchdog"

    defaultConfig {
        applicationId = "com.m.twitchwatchdog"
        versionCode = 1
        versionName = "1.0"
        archivesName = "twitchwatchdog-v${versionName}-${versionCode}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":shared"))

    implementation(libs.androidx.core.ktx)

    implementation(libs.jsoup)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.kotlinx.coroutines.core)

    // Coil
    implementation(libs.coil.compose)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Moshi
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)

    // Lottie
    implementation(libs.lottie.compose)

    // DataStore
    implementation(libs.androidx.datastore)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.truth)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}