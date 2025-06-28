package com.london.buildsrc

import org.gradle.api.JavaVersion

object AppConfig {

    const val ENABLE_R8_FULL_MODE: Boolean = true
    const val IS_RELEASE_MODE_DEBUGGABLE: Boolean = false

    object Version {
        const val MIN_SDK = 26
        const val TARGET_SDK = 34
        const val COMPILE_SDK = 35
        val JVM = JavaVersion.VERSION_17
        const val BUILD_TOOLS = "35.0.0"
    }

    private const val APPLICATION_ID_GROUP = "com.london"
    private const val APPLICATION_ID_SUFFIX = "novix"
    const val APPLICATION_ID = "${APPLICATION_ID_GROUP}.${APPLICATION_ID_SUFFIX}"
    const val ANDROID_TEST_INSTRUMENTATION = "androidx.test.runner.AndroidJUnitRunner"
}