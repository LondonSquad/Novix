package com.london.data.utils

import com.london.data.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLBuilder
import io.ktor.http.path

fun Int?.orZero() = this ?: 0

fun Double?.orZero() = this ?: 0.0

val Boolean?.isTrue
    get() = this == true

fun Double?.roundToFirstDecimal(): String = "%.1f".format(this)

fun String?.asImageUrlOrEmpty() = this?.let { BuildConfig.IMAGE_URL + it }.orEmpty()

suspend inline fun <reified T> HttpClient.get(
    path: String,
    params: Map<String, String> = emptyMap(),
    crossinline block: URLBuilder.() -> Unit = {}
): T = get {
    url {
        path(path)
        params.forEach { key, value ->
            parameters.append(key, value)
        }
        block(this)
    }
}.body()
