package com.london.domain.usecase.authentication

import com.london.domain.repository.AuthenticationRepository
import javax.inject.Inject

class AuthenticationUseCase @Inject constructor(
    private val repository: AuthenticationRepository
) {
    suspend fun logout() = repository.logout()

    suspend fun isLoggedIn() = repository.isLoggedIn()

    suspend fun loginAsGuest() = repository.loginAsGuest()

    suspend fun login(username: String, password: String) : Boolean {
        return repository.login(
            username = username,
            password = password
        )
    }
}
