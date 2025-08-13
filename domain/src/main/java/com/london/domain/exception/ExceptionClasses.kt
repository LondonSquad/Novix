package com.london.domain.exception

class ConnectionException(message: String? = "Connection failed") : Exception(message)

class InternetDisconnectedException(message: String? = "No internet connection") :
    Exception(message)

class UnAuthorizedException(message: String? = "Unauthorized access") : Exception(message)

class EmptyBodyException(message: String? = "Response body is empty") : Exception(message)

class ValidationException(message: String? = "Validation failed") : Exception(message)

class EntryNotFoundException(message: String? = "Entry not found") : Exception(message)

class ResponseException(
    val code: Int,
    message: String? = "Response error"
) : Exception(message)
