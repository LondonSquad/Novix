plugins {
    `java-library`
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.android.lint)
}

lint {
    htmlReport = true
    htmlOutput = file("lint-report.html")
    textReport = true
    absolutePaths = false
    abortOnError = true
    ignoreTestSources = true
}

dependencies {
    compileOnly(libs.lint.api)
}
