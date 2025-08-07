package com.london.domain.repository

interface AccountRepository {
    suspend fun getUserName(): String
}