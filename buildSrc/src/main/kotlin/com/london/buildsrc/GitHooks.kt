package com.london.buildsrc

import org.gradle.api.Project

fun Project.configureGitHooks() {
    tasks.register("installGitHooks") {
        doLast {
            installHook("commit-message", "commit-msg")
            installHook("branch-naming", "pre-push")
        }
    }

    gradle.projectsEvaluated {
        tasks.findByName("build")?.dependsOn("installGitHooks")
    }
}

fun Project.installHook(sourceFile: String, targetFile: String) {
    val source = rootProject.file("scripts/hooks/$sourceFile")
    val target = rootProject.file(".git/hooks/$targetFile")

    if (!target.exists() || target.readText() != source.readText()) {
        target.writeText(source.readText())
        target.setExecutable(true)
        println("✅ $targetFile hook installed")
    } else {
        println("✅ $targetFile hook up to date")
    }
}

