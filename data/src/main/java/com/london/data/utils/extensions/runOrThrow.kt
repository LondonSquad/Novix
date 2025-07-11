package com.london.data.utils.extensions


suspend fun <T : Any> runOrThrow(
    block: suspend () -> T,
    onFailure: (Throwable) -> Throwable
): T = runCatching { block() }.getOrElse { throw onFailure(it) }
