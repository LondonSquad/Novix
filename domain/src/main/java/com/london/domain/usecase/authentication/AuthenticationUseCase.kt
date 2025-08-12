package com.london.domain.usecase.authentication

import com.london.domain.repository.AuthenticationRepository
import javax.inject.Inject

class AuthenticationUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun loginAsGuest() = authenticationRepository.loginAsGuest()

    suspend fun login(username: String, password: String) =
        authenticationRepository.login(username, password)

    suspend fun logout() = authenticationRepository.logout()

    suspend fun isLoggedIn() = authenticationRepository.isLoggedIn()
}