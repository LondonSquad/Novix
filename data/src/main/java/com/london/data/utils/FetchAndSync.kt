package com.london.data.utils

suspend fun <T> Result<T?>.getNotNullOrElse(elseBlock: suspend () -> T): Result<T> =
    runCatching { getOrElse { elseBlock() } ?: elseBlock() }

suspend fun <T> fetchAndSync(
    cacheBlock: (suspend () -> T?)? = null,
    networkBlock: suspend () -> T,
    syncBlock: (suspend (T) -> Unit)? = null,
    crashReporter: CrashReporter? = null
): T = runCatching { cacheBlock?.invoke() }.getNotNullOrElse {
    networkBlock().also {
        syncBlock?.invoke(it)
    }
}.onFailure { crashReporter?.logException(it) }.getOrThrow()