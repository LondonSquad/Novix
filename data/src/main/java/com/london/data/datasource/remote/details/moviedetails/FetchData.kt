package com.london.data.datasource.remote.details.moviedetails

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path

suspend inline fun <reified T> fetchData(
    path: String,
    ktorClient: HttpClient
): T {
    return runCatching {
        val response = ktorClient.get {
            url {
                path(path)
            }
        }
        response.body<T>()
    }.getOrElse { exception ->
        throw exception
    }
}
