import com.london.buildsrc.AppConfig

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.kover)
}

android {
    namespace =  AppConfig.Namespace.DATA
    compileSdk = AppConfig.Version.COMPILE_SDK

    defaultConfig {
        minSdk = AppConfig.Version.MIN_SDK

        testInstrumentationRunner = AppConfig.ANDROID_TEST_INSTRUMENTATION
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = AppConfig.ENABLE_R8_FULL_MODE
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
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.bundles.base.ui)
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.datastore)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.room)
    ksp(libs.bundles.room.ksp)
    implementation(libs.bundles.koin)
    ksp(libs.bundles.koin.ksp)
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.android.testing)
}