package com.london.data.datasource.local


class InsertException(message: String = "Failed to insert %s into the local database.") : Exception(message)
class UpdateException(message: String = "Failed to update %s in the local database.") : Exception(message)
class DeleteException(message: String = "Failed to delete %s from the local database.") : Exception(message)
class GetException(message: String = "Failed to insert %s into the local database.") : Exception(message)
