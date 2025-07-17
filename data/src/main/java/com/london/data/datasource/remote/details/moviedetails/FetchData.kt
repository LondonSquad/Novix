package com.london.data.datasource.remote.details.moviedetails

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.path
import kotlinx.serialization.json.Json

val json = Json{ignoreUnknownKeys = true}

suspend inline fun <reified T> fetchData(
    path: String,
    tag: String,
    ktorClient: HttpClient
): T {
    return runCatching {
        val response = ktorClient.get {
            url {
                path(path)
            }
        }
        val responseBody = response.bodyAsText()
        Log.d("MovieDetailsRemoteImpl", "$tag response: $responseBody")
        json.decodeFromString<T>(responseBody)
    }.getOrElse { exception ->
        Log.e("MovieDetailsRemoteImpl", "Error in $tag", exception)
        throw exception
    }
}
