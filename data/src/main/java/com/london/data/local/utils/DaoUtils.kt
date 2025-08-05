package com.london.data.local.utils

import com.london.data.local.database.dao.home.HomeDao
import com.london.data.local.exception.DeleteException
import com.london.data.local.exception.GetException
import com.london.data.local.exception.InsertException

suspend inline fun <T> runOrThrow(
    crossinline block: suspend () -> T, crossinline error: () -> Throwable
): T = runCatching { block() }.getOrElse { throw error() }

fun checkIfOneHourExpired(date: Long): Boolean {
    val oneHourAgo = System.currentTimeMillis() - (3600000)
    return date < oneHourAgo
}

suspend fun <T: Any> HomeDao<T>.executeInsert(input: T, errorMessage: String? = null) =
    runOrThrow(block = { insert(input) }, error = {
        InsertException(
            errorMessage ?: "Failed to insert ${input::class.simpleName} into the local database."
        )
    })

suspend fun <T: Any> HomeDao<T>.executeInsertAll(input: List<T>, errorMessage: String? = null) =
    runOrThrow(block = { insertAll(input) }, error = {
        InsertException(
            errorMessage ?: "Failed to insert ${input::class.simpleName} into the local database."
        )
    })

suspend fun <T: Any> HomeDao<T>.executeDeleteAll(errorMessage: String? = null) =
    runOrThrow(block = { deleteAll() }, error = {
        DeleteException(
            errorMessage ?: "Failed to delete the list from the local database."
        )
    })

suspend inline fun <reified T : Any> HomeDao<T>.executeGetAll(errorMessage: String? = null) =
    runOrThrow(block = { getAll() }, error = {
        GetException(
            errorMessage ?: "Failed to get ${T::class.simpleName} from the local database."
        )
    })
