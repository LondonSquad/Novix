import com.london.buildsrc.AppConfig

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = AppConfig.Namespace.DESIGN_SYSTEM
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
    implementation(libs.bundles.compose)
    implementation(libs.bundles.base.ui)
    implementation(libs.bundles.ui.utils)
    debugImplementation(libs.bundles.compose.debug)
    androidTestImplementation(libs.bundles.base.testing)
}