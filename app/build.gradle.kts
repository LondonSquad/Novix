import com.london.buildsrc.AppConfig
import com.london.buildsrc.configureGitHooks

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = AppConfig.APPLICATION_ID
    compileSdk = AppConfig.Version.COMPILE_SDK

    defaultConfig {
        applicationId = AppConfig.APPLICATION_ID
        minSdk = AppConfig.Version.MIN_SDK
        targetSdk = AppConfig.Version.TARGET_SDK
        versionCode = 1
        versionName = "1.0"

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

configureGitHooks()

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":designSystem"))
    implementation(project(":presentation"))

    implementation(libs.bundles.androidx.core)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    debugImplementation(libs.bundles.compose.debug)

    implementation(libs.bundles.koin)
    ksp(libs.bundles.koin.ksp)

    implementation(libs.bundles.datastore)

    implementation(libs.bundles.room)
    ksp(libs.bundles.room.ksp)

    implementation(libs.bundles.ktor)

    implementation(libs.bundles.coroutines)

    implementation(libs.bundles.ui.utils)

    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.android.testing)
}


