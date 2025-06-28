package com.london.buildsrc

import org.gradle.api.artifacts.dsl.DependencyHandler


fun DependencyHandler.addAndroidCore() {
    implementation(Deps.AndroidX.CORE_KTX)
    implementation(Deps.AndroidX.LIFECYCLE_RUNTIME)
    implementation(Deps.AndroidX.ACTIVITY_COMPOSE)
    implementation(Deps.AndroidX.SPLASH_SCREEN)
}

fun DependencyHandler.addComposeUI() {
    implementation(platform(Deps.Compose.BOM))
    implementation(Deps.Compose.UI)
    implementation(Deps.Compose.GRAPHICS)
    implementation(Deps.Compose.TOOLING)
    implementation(Deps.Compose.PREVIEW)
    implementation(Deps.Compose.MATERIAL3)
    implementation(Deps.Compose.NAVIGATION)
}

fun DependencyHandler.addUIHelpers() {
    implementation(Deps.UI.COIL)
    implementation(Deps.UI.LOTTIE)
    implementation(Deps.UI.REMEMBER_PREFERENCE)
}

fun DependencyHandler.addNetworking() {
    implementation(Deps.Ktor.CORE)
    implementation(Deps.Ktor.ANDROID)
    implementation(Deps.Ktor.SERIALIZATION)
    implementation(Deps.Ktor.LOGGING)
}

fun DependencyHandler.addDatabase() {
    implementation(Deps.Room.RUNTIME)
    implementation(Deps.Room.KTX)
    implementation(Deps.Room.RXJAVA3)
    ksp(Deps.Room.COMPILER)
}

fun DependencyHandler.addStorage() {
    implementation(Deps.Datastore.PREFERENCES)
    implementation(Deps.Datastore.CORE)
}

fun DependencyHandler.addDI() {
    implementation(Deps.Koin.ANDROID)
    implementation(Deps.Koin.COMPOSE)
    implementation(Deps.Koin.ANNOTATIONS)
    ksp(Deps.Koin.COMPILER)
}

fun DependencyHandler.addKotlinx() {
    implementation(Deps.Kotlinx.COROUTINES)
    implementation(Deps.Kotlinx.DATETIME)
}

fun DependencyHandler.addUnitTesting() {
    testImplementation(Deps.Testing.JUNIT)
    testImplementation(Deps.Testing.JUPITER)
    testImplementation(Deps.Testing.MOCKK)
    testImplementation(Deps.Kotlinx.COROUTINES_TEST)
    testImplementation(Deps.Testing.TRUTH)
}

fun DependencyHandler.addAndroidTesting() {
    androidTestImplementation(Deps.Testing.EXT)
    androidTestImplementation(Deps.Testing.ESPRESSO)
    androidTestImplementation(Deps.Testing.UI_TEST_JUNIT4)
}

fun DependencyHandler.addDebugDependencies() {
    debugImplementation(Deps.Testing.UI_TEST_MANIFEST)
}

fun DependencyHandler.implementation(dep: Any) = add("implementation", dep)
fun DependencyHandler.ksp(dep: Any) = add("ksp", dep)
fun DependencyHandler.testImplementation(dep: Any) = add("testImplementation", dep)
fun DependencyHandler.androidTestImplementation(dep: Any) = add("androidTestImplementation", dep)
fun DependencyHandler.debugImplementation(dep: Any) = add("debugImplementation", dep)