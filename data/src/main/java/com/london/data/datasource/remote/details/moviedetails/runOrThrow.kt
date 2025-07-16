package com.london.data.datasource.remote.details.moviedetails

suspend inline fun <T> runOrThrow(
    crossinline block: suspend () -> T,
    crossinline error: (Throwable) -> Throwable
): T = runCatching {
    block()
}.getOrElse { throwable ->
    throw error(throwable)
}
