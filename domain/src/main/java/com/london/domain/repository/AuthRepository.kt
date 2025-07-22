package com.london.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(username: String, password: String): Flow<Boolean>
    fun loginAsGuest(): Flow<Boolean>
    fun logout(): Flow<Boolean>
    fun isLoggedIn(): Boolean
    fun validateSession(): Flow<Boolean>
}