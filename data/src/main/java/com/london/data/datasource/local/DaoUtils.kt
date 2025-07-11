package com.london.data.datasource.local

import com.london.data.datasource.local.dao.SearchDao

suspend fun <T : Any> SearchDao<T>.executeInsert(input: T, errorMassege: String? = null) =
    runOrThrow(block = { insert(input) }, error = {
        InsertException(
            errorMassege ?: "Failed to insert ${input::class.simpleName} into the local database."
        )
    })

suspend fun <T : Any> SearchDao<T>.executeUpdate(input: T, errorMassege: String? = null) =
    runOrThrow(block = { update(input) }, error = {
        UpdateException(
            errorMassege ?: "Failed to update ${input::class.simpleName} in the local database."
        )
    })

suspend fun <T : Any> SearchDao<T>.executeDelete(input: T, errorMassege: String? = null) =
    runOrThrow(block = { delete(input) }, error = {
        DeleteException(
            errorMassege ?: "Failed to delete ${input::class.simpleName} from the local database."
        )
    })

suspend inline fun <reified T : Any> SearchDao<T>.executeGetAll(errorMassege: String? = null) =
    runOrThrow(block = { getAll() }, error = {
        GetException(
            errorMassege ?: "Failed to get ${T::class.simpleName} from the local database."
        )
    })

suspend fun <T : Any> SearchDao<T>.executeGetByQuery(
    input: String, errorMassege: String? = null
) = runOrThrow(block = { getSearchByQuery(input) }, error = {
    GetException(
        errorMassege ?: "Failed to get ${input::class.simpleName} from the local database."
    )
})

suspend fun <T : Any> SearchDao<T>.executeGetByDate(
    input: Long, errorMassege: String? = null
) = runOrThrow(block = { getCurrentSearchByDate(input) }, error = {
    GetException(
        errorMassege ?: "Failed to get ${input::class.simpleName} from the local database."
    )
})

suspend inline fun <T> runOrThrow(
    crossinline block: suspend () -> T, crossinline error: () -> Throwable
): T = runCatching { block() }.getOrElse { throw error() }