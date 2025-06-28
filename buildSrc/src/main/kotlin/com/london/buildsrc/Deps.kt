package com.london.buildsrc

object Deps {

    object AndroidX {
        const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
        const val LIFECYCLE_RUNTIME =
            "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE_RUNTIME_KTX}"
        const val ACTIVITY_COMPOSE =
            "androidx.activity:activity-compose:${Versions.ACTIVITY_COMPOSE}"
        const val SPLASH_SCREEN = "androidx.core:core-splashscreen:${Versions.SPLASH_SCREEN}"
    }

    object Compose {
        const val BOM = "androidx.compose:compose-bom:${Versions.COMPOSE_BOM}"
        const val UI = "androidx.compose.ui:ui:${Versions.COMPOSE_UI}"
        const val GRAPHICS = "androidx.compose.ui:ui-graphics:${Versions.COMPOSE_UI}"
        const val TOOLING = "androidx.compose.ui:ui-tooling:${Versions.COMPOSE_UI}"
        const val PREVIEW = "androidx.compose.ui:ui-tooling-preview:${Versions.COMPOSE_UI}"
        const val MATERIAL3 = "androidx.compose.material3:material3:${Versions.MATERIAL3}"
        const val NAVIGATION =
            "androidx.navigation:navigation-compose:${Versions.NAVIGATION_COMPOSE}"
    }

    object Room {
        const val RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
        const val KTX = "androidx.room:room-ktx:${Versions.ROOM}"
        const val COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"
        const val RXJAVA3 = "androidx.room:room-rxjava3:${Versions.ROOM}"
    }

    object Datastore {
        const val PREFERENCES = "androidx.datastore:datastore-preferences:${Versions.DATASTORE}"
        const val CORE = "androidx.datastore:datastore-core:${Versions.DATASTORE}"
    }

    object Koin {
        const val ANDROID = "io.insert-koin:koin-android:${Versions.KOIN}"
        const val COMPOSE = "io.insert-koin:koin-androidx-compose:${Versions.KOIN}"
        const val ANNOTATIONS = "io.insert-koin:koin-annotations:${Versions.KOIN_ANNOTATIONS}"
        const val COMPILER = "io.insert-koin:koin-ksp-compiler:${Versions.KOIN_ANNOTATIONS}"
    }

    object Ktor {
        const val CORE = "io.ktor:ktor-client-core:${Versions.KTOR}"
        const val ANDROID = "io.ktor:ktor-client-okhttp:${Versions.KTOR}"
        const val SERIALIZATION = "io.ktor:ktor-serialization-kotlinx-json:${Versions.KTOR}"
        const val LOGGING = "io.ktor:ktor-client-logging:${Versions.KTOR}"
    }

    object Kotlinx {
        const val COROUTINES =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}"
        const val COROUTINES_TEST =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES_TEST}"
        const val DATETIME = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.DATETIME}"
    }

    object UI {
        const val COIL = "io.coil-kt:coil-compose:${Versions.COIL}"
        const val LOTTIE = "com.airbnb.android:lottie-compose:${Versions.LOTTIE}"
        const val REMEMBER_PREFERENCE =
            "dev.burnoo:compose-remember-preference:${Versions.REMEMBER_PREFERENCE}"
    }

    object Testing {
        const val JUNIT = "junit:junit:${Versions.JUNIT}"
        const val JUPITER = "org.junit.jupiter:junit-jupiter:${Versions.JUNIT_JUPITER}"
        const val EXT = "androidx.test.ext:junit:${Versions.JUNIT_EXT}"
        const val ESPRESSO = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO}"
        const val UI_TEST_JUNIT4 = "androidx.compose.ui:ui-test-junit4:${Versions.COMPOSE_UI}"
        const val UI_TEST_MANIFEST = "androidx.compose.ui:ui-test-manifest:${Versions.COMPOSE_UI}"
        const val MOCKK = "io.mockk:mockk:${Versions.MOCKK}"
        const val TRUTH = "com.google.truth:truth:${Versions.TRUTH}"
    }
}
