package com.london.domain.usecase

import com.london.domain.repository.AuthRepository
import javax.inject.Inject

class LoggedInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun invoke() = authRepository.isLoggedIn()
}