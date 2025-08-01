package com.london.domain.usecase.login

import com.london.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun invoke() = authRepository.logout()
}