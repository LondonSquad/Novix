package com.london.domain.usecase.login

import com.london.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun invoke(username: String, password: String) =
        authRepository.login(username, password)
}