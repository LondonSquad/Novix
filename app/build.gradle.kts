plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.london.novix"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.london.novix"
        minSdk = 26
        targetSdk = 35
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
    addAndroidCore()
    addCompose()
    addDatabaseAndStorage()
    addDependencyInjection()
    addNetworking()
    addImageLoading()
    addUtilities()
    addTesting()
    addAndroidTesting()
    addDebug()
}

fun DependencyHandlerScope.addAndroidCore() {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.splashscreen)
}

fun DependencyHandlerScope.addCompose() {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.lottie.compose)
}


fun DependencyHandlerScope.addDatabaseAndStorage() {
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.rxjava3)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.core)
    ksp(libs.androidx.room.compiler)
}

fun DependencyHandlerScope.addDependencyInjection() {
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)
}

fun DependencyHandlerScope.addNetworking() {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.logging)
}

fun DependencyHandlerScope.addImageLoading() {
    implementation(libs.coil.compose)
}

fun DependencyHandlerScope.addUtilities() {
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.remember.preference)
}

fun DependencyHandlerScope.addTesting() {
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.truth)
    testImplementation(kotlin("test"))
}

fun DependencyHandlerScope.addAndroidTesting() {
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
}

fun DependencyHandlerScope.addDebug() {
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

// Git Hooks Setup
tasks.register("installGitHooks") {
    doLast {
        installHook("commit-message", "commit-msg")
        installHook("branch-naming", "pre-push")
    }
}

fun installHook(sourceFile: String, targetFile: String) {
    val source = file("../scripts/hooks/$sourceFile")
    val target = file("../.git/hooks/$targetFile")

    if (!target.exists() || target.readText() != source.readText()) {
        target.writeText(source.readText())
        target.setExecutable(true)
        println("✅ $targetFile hook installed")
    } else {
        println("✅ $targetFile hook up to date")
    }
}

gradle.projectsEvaluated {
    tasks["build"].dependsOn("installGitHooks")
}