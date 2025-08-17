import com.london.buildsrc.AppConfig

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.london.imageharamblur"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = AppConfig.ENABLE_R8_FOR_LIBRARIES
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

    aaptOptions {
        noCompress += "tflite"
    }

    lint {
        disable += "CoroutineCreationDuringComposition"
        disable += "StateFlowValueCalledInComposition"
        disable += "FlowOperatorInvokedInComposition"
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // TensorFlow Lite
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite)

    // Coil
    implementation(libs.coil.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Firebase ML Model downloader
    implementation(libs.firebase.ml.modeldownloader)

}