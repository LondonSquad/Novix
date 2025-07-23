package com.london.domain.usecase.login

import com.london.domain.repository.AuthRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class LogoutUseCase(
    @Provided
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.logout()
}