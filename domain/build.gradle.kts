import com.london.buildsrc.AppConfig

plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.ksp)
}

java {
    sourceCompatibility = AppConfig.Version.JVM
    targetCompatibility = AppConfig.Version.JVM
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    api(libs.bundles.coroutines)
    api(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)
    testImplementation(libs.bundles.testing)
}
