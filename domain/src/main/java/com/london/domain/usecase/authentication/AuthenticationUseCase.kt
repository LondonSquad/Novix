package com.london.domain.usecase.authentication

import com.london.domain.repository.AuthRepository
import javax.inject.Inject

class AuthenticationUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun loginAsGuest() = authRepository.loginAsGuest()

    suspend fun login(username: String, password: String) =
        authRepository.login(username, password)

    suspend fun logout() = authRepository.logout()

    suspend fun isLoggedIn() = authRepository.isLoggedIn()
}