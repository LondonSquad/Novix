package com.london.data.datasource.local


open class BaseException(override var message: String) : Exception(message)
class InsertException : BaseException("Failed to insert %s into the local database.")
class UpdateException : BaseException("Failed to update %s in the local database.")
class DeleteException : BaseException("Failed to delete %s from the local database.")
class GetException : BaseException("Failed to insert %s into the local database.")
