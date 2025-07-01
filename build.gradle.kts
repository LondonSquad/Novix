import com.london.buildsrc.configureGitHooks

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
                    classes("*.domain.*")
                    classes("**ViewModel")
                    classes("**viewModel")
                    classes("**viewmodel")
                    classes("**Viewmodel")
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
