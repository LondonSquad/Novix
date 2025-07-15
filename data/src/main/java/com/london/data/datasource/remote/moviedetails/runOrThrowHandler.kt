package com.london.data.datasource.remote.moviedetails

suspend inline fun <T> runOrThrowHandler(
    crossinline block: suspend () -> T,
    crossinline error: (Throwable) -> Throwable
): T = runCatching {
    block()
}.getOrElse { throwable ->
    throw error(throwable)
}
