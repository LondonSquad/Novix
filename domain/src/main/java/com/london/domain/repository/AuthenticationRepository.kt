package com.london.domain.repository

interface AuthenticationRepository {

    suspend fun login(username: String, password: String): Boolean

    suspend fun loginAsGuest(): Boolean

    suspend fun logout(): Boolean

    suspend fun isLoggedIn(): Boolean

}
