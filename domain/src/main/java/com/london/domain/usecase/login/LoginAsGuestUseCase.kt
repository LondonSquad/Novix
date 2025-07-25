package com.london.domain.usecase.login

import com.london.domain.repository.AuthRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class LoginAsGuestUseCase(
    @Provided
    private val authRepository: AuthRepository
) {
    suspend fun invoke() = authRepository.loginAsGuest()
}