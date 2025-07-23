package com.london.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(username: String, password: String): Boolean // Or Result<Unit> for more detail
    suspend fun loginAsGuest(): Boolean // Or Result<Unit>
    suspend fun logout(): Boolean // Or Result<Unit>
    fun isLoggedIn(): Boolean // This can remain synchronous if it's a quick check
    fun validateSession(): Flow<Boolean> // Keep as Flow if session validation can emit multiple states or is long-lived
}