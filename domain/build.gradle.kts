import com.london.buildsrc.AppConfig

plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.kover)
}

java {
    sourceCompatibility = AppConfig.Version.JVM
    targetCompatibility = AppConfig.Version.JVM
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

dependencies {
    api(libs.bundles.coroutines)
    api(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)
    testImplementation(libs.bundles.testing)
    api(libs.koin.core)
    ksp(libs.bundles.koin.ksp)
}

ksp {
    arg("KOIN_CONFIG_CHECK", "true")
    arg("KOIN_DEFAULT_MODULE", "false")
    arg("KOIN_USE_COMPOSE_VIEWMODEL", "true")
}
