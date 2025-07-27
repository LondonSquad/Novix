package com.london.data.utils

import com.london.domain.error.NetworkException
import retrofit2.HttpException
import java.io.IOException

suspend inline fun <reified T> safeCallApi(
    crossinline call: suspend () -> T
): T {
    return try {
        call()
    } catch (e: HttpException) {
        val errorMessage = when (e.code()) {
            401 -> "Unauthorized access"
            403 -> "Forbidden access"
            404 -> "Resource not found"
            500 -> "Server error"
            502, 503 -> "Service unavailable"
            else -> "HTTP error: ${e.code()}"
        }
        throw RuntimeException(errorMessage, e)
    } catch (e: IOException) {
        throw NetworkException.NoInternetException("Network connection failed: ${e.message}", e)
    }
}