package com.london.domain.usecase.login

import com.london.domain.repository.AuthRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class LoginUseCase(
    @Provided
    private val authRepository: AuthRepository
) {
    suspend fun invoke(username: String, password: String) =
        authRepository.login(username, password)
}