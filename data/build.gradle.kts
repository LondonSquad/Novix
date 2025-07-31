import com.london.buildsrc.AppConfig
import com.london.buildsrc.getKey

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = AppConfig.Namespace.DATA
    compileSdk = AppConfig.Version.COMPILE_SDK

    defaultConfig {
        minSdk = AppConfig.Version.MIN_SDK

        testInstrumentationRunner = AppConfig.ANDROID_TEST_INSTRUMENTATION
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "API_KEY", getKey("API_KEY"))
        buildConfigField("String", "IMAGE_URL", getKey("IMAGE_URL"))
        buildConfigField("String", "BASE_URL", getKey("BASE_URL"))
        buildConfigField("String", "AUTHORIZATION_KEY", getKey("AUTHORIZATION_KEY"))
        buildConfigField("String", "YOUTUBE_URL", getKey("YOUTUBE_URL"))
    }

    buildFeatures{
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = AppConfig.ENABLE_R8_FOR_LIBRARIES
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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
}

dependencies {
    implementation(projects.domain)
    implementation(libs.bundles.base.ui)
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.datastore)
    implementation(libs.bundles.room)
    ksp(libs.bundles.room.ksp)
    implementation(libs.gson)
    implementation(libs.firebase.crashlytics)

    //Testing
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation (libs.truth)

    // Test dependencies - properly scoped
    testImplementation(libs.bundles.testing)
    testImplementation(kotlin("test"))

    // Android test dependencies
    androidTestImplementation(libs.bundles.android.testing)

    // Retrofit
    implementation(libs.bundles.retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json)
    ksp(libs.hilt.compiler)

    implementation(libs.hilt.android)

    implementation(libs.timber)
}
