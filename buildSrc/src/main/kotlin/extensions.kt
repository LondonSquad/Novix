package com.london.buildscr

import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties


fun getLocalProperty(key: String, file: String? = null, root: String = "."): String {
    val properties = Properties()
    val defaultFiles = listOf("$root/local.properties", "$root/defaults.properties")
    val files = (defaultFiles + file).mapNotNull { it }

    files.forEach {
        val localProperties = File(it)
        if (localProperties.isFile) {
            runCatching {
                InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
                    if (properties[key] in listOf(null, ""))
                        properties.load(reader)
                }
            }
        }
    }

    return properties.getProperty(key).toString()
}

fun Project.getKey(key: String) = getLocalProperty(key, root = rootDir.path)
