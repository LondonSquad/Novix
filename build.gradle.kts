@file:Suppress("OPT_IN_USAGE")

import com.london.buildsrc.AppConfig
import com.london.buildsrc.configureGitHooks
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // Ksp
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.firebase.firebase.perf) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.google.firebase.crashlytics) apply false
    alias(libs.plugins.kotlinx.kover) apply true
}

subprojects {
    plugins.withId("org.jetbrains.kotlin.android") {
        extensions.configure<KotlinAndroidProjectExtension> {
            compilerOptions {
                freeCompilerArgs.addAll(AppConfig.freeCompilerArgs)
            }
        }
    }
}

dependencies {
    kover(project(":app"))
    kover(project(":domain"))
    kover(project(":data"))
    kover(project(":presentation"))
    kover(project(":designSystem"))
}
kover {
    reports {
        total {
            filters {
                includes {
                    packages(
                        "com.london.data.mapper",
                        "com.london.domain.usecase",
                        "com.london.data.repository",
                        "com.london.data.datasource.local.search",
                        "com.london.data.datasource.local.recent",
                    )
                    // TODO: Uncomment this line to cover viewModels
                    // classes("**.*ViewModel")
                }

                excludes {
                    annotatedBy("com.london.domain.KoverIgnore")
                    packages(
                        "org.koin.ksp.generated.**",
                        "com.london.data.datasource.remote.**"
                    )
                    classes("**.*RemoteDataSourceImpl")
                    classes("**RemoteDataSourceImpl")
                }
            }
            verify {
                rule {
                    bound {
                        minValue = 80
                    }
                }
            }
        }
    }
}
configureGitHooks()
