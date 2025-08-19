package com.london.data.remote.exception

data class ResponseException(
    override val message: String?,
    val code: Int
) : Exception(message)
