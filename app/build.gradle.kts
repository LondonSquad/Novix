import com.london.buildsrc.AppConfig

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.firebase.firebase.perf)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.kotlinx.kover)
}

android {
    namespace = AppConfig.APPLICATION_ID
    compileSdk = AppConfig.Version.COMPILE_SDK

    defaultConfig {
        applicationId = AppConfig.APPLICATION_ID
        minSdk = AppConfig.Version.MIN_SDK
        targetSdk = AppConfig.Version.TARGET_SDK

        // Allows for setting the version code via a Gradle property for CD pipeline.
        versionCode = (project.findProperty("versionCode") as? String)?.toInt() ?: 1
        versionName = project.findProperty("versionName") as? String ?: "1.0"

        testInstrumentationRunner = AppConfig.ANDROID_TEST_INSTRUMENTATION
    }

    buildTypes {
        release {
            isMinifyEnabled = AppConfig.ENABLE_R8_FULL_MODE
            isDebuggable = AppConfig.IS_RELEASE_MODE_DEBUGGABLE
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = AppConfig.Version.JVM
        targetCompatibility = AppConfig.Version.JVM
    }
    kotlinOptions {
        jvmTarget = AppConfig.Version.JVM.toString()
    }
    buildFeatures {
        compose = true
    }
}


dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":presentation"))
    implementation(project(":designSystem"))
    implementation(libs.bundles.base.ui)
    implementation(libs.bundles.koin)
    ksp(libs.bundles.koin.ksp)
    debugImplementation(libs.bundles.compose.debug)
    androidTestImplementation(libs.bundles.base.testing)
    androidTestImplementation(libs.bundles.android.testing)
    implementation(libs.firebase.perf)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    testImplementation(libs.bundles.testing)
}