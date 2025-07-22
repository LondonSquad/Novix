package com.london.domain.usecase.login

import com.london.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class LoginAsGuestUseCase(
    @Provided
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return authRepository.loginAsGuest()
    }
}