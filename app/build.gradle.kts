import com.london.buildsrc.AppConfig
import com.london.buildsrc.addAndroidCore
import com.london.buildsrc.addAndroidTesting
import com.london.buildsrc.addComposeUI
import com.london.buildsrc.addDI
import com.london.buildsrc.addDatabase
import com.london.buildsrc.addDebugDependencies
import com.london.buildsrc.addKotlinx
import com.london.buildsrc.addNetworking
import com.london.buildsrc.addStorage
import com.london.buildsrc.addUIHelpers
import com.london.buildsrc.addUnitTesting
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
    addAndroidCore()
    addComposeUI()
    addUIHelpers()
    addNetworking()
    addDatabase()
    addStorage()
    addDI()
    addKotlinx()
    addUnitTesting()
    addAndroidTesting()
    addDebugDependencies()
}

