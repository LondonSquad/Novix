package com.london.data.local.utils

import com.london.data.local.database.dao.home.popular.HomeDao
import com.london.data.local.database.dao.SearchDao
import com.london.data.local.exception.DeleteException
import com.london.data.local.exception.GetException
import com.london.data.local.exception.InsertException
import com.london.data.local.exception.UpdateException

suspend fun <T : Any> SearchDao<T>.executeInsert(input: T, errorMessage: String? = null) =
    runOrThrow(block = { insert(input) }, error = {
        InsertException(
            errorMessage ?: "Failed to insert ${input::class.simpleName} into the local database."
        )
    })

suspend fun <T : Any> SearchDao<T>.executeUpdate(input: T, errorMessage: String? = null) =
    runOrThrow(block = { update(input) }, error = {
        UpdateException(
            errorMessage ?: "Failed to update ${input::class.simpleName} in the local database."
        )
    })

suspend fun <T : Any> SearchDao<T>.executeDelete(input: T, errorMessage: String? = null) =
    runOrThrow(block = { delete(input) }, error = {
        DeleteException(
            errorMessage ?: "Failed to delete ${input::class.simpleName} from the local database."
        )
    })

suspend inline fun <reified T : Any> SearchDao<T>.executeGetAll(errorMessage: String? = null) =
    runOrThrow(block = { getAll() }, error = {
        GetException(
            errorMessage ?: "Failed to get ${T::class.simpleName} from the local database."
        )
    })

suspend fun <T : Any> SearchDao<T>.executeGetByQuery(
    input: String, errorMessage: String? = null
) = runOrThrow(block = { getSearchByQuery(input) }, error = {
    GetException(
        errorMessage ?: "Failed to get ${input::class.simpleName} from the local database."
    )
})

suspend fun <T : Any> SearchDao<T>.executeGetByQueryAndPage(
    input: String, page: Int, errorMessage: String? = null
) = runOrThrow(block = { getSearchByQueryAndPage(input, page) }, error = {
    GetException(
        errorMessage ?: "Failed to get ${input::class.simpleName} from the local database."
    )
})

suspend fun <T : Any> SearchDao<T>.executeGetByDate(
    input: Long, errorMessage: String? = null
) = runOrThrow(block = { getCurrentSearchByDate(input) }, error = {
    GetException(
        errorMessage ?: "Failed to get ${input::class.simpleName} from the local database."
    )
})

suspend inline fun <T> runOrThrow(
    crossinline block: suspend () -> T, crossinline error: () -> Throwable
): T = runCatching { block() }.getOrElse { throw error() }

suspend fun <T: Any> HomeDao<T>.executeInsert(input: T, errorMessage: String? = null) =
    runOrThrow(block = { insert(input) }, error = {
        InsertException(
            errorMessage ?: "Failed to insert ${input::class.simpleName} into the local database."
        )
    })


