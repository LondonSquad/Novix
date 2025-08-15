@file:Suppress("OPT_IN_USAGE")

import com.london.buildsrc.AppConfig
import com.london.buildsrc.configureGitHooks
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.firebase.firebase.perf) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.google.firebase.crashlytics) apply false
    alias(libs.plugins.kotlinx.kover)
}

subprojects {
    apply(plugin = "org.jetbrains.kotlinx.kover")

    plugins.withId("org.jetbrains.kotlin.android") {
        extensions.configure<KotlinAndroidProjectExtension> {
            compilerOptions {
                freeCompilerArgs.addAll(AppConfig.freeCompilerArgs)
            }
        }
    }
}

dependencies {
    kover(projects.domain)
    kover(projects.data)
    kover(projects.presentation)
}

kover {
    reports {
        total {
            filters {
                includes {
                    packages(
                        "com.london.domain.**",
                        "com.london.data.**",
                        "com.london.presentation.**"
                    )
                    classes("**.*ViewModel")
                }

                excludes {
                    // Annotation-based exclusions
                    annotatedBy("com.london.domain.KoverIgnore")

                    // Package exclusions
                    packages(
                        "com.london.data.datasource.remote.**",
                        "com.london.imageharamblur.**",
                        "**.*di.*",
                        "**.di.**"
                    )

                    // Class pattern exclusions
                    classes(
                        "*di.*",
                        "**.di.**",
                        "**.*Activity",
                        "**.*Fragment",
                        "**.*Application",
                        "**.*Module",
                        "**.*Component",
                        "**.*_Factory",
                        "**.*_HiltModules*",
                        "**.BuildConfig",
                        "**.*ComposableSingletons*",
                        "**.*_Impl*"
                    )
                }
            }

            verify {
                rule {
                    bound { minValue = 80 }
                }
            }

            xml { onCheck = false }

            html { onCheck = false }
        }
    }
}

configureGitHooks()
