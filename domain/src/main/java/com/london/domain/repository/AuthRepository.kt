package com.london.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(username: String, password: String): Boolean
    suspend fun loginAsGuest(): Boolean
    suspend fun logout(): Boolean
    fun isLoggedIn(): Boolean
    fun validateSession(): Flow<Boolean>
}