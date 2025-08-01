package com.london.domain.usecase.login

import com.london.domain.repository.AuthRepository
import javax.inject.Inject

class LoginAsGuestUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun invoke() = authRepository.loginAsGuest()
}