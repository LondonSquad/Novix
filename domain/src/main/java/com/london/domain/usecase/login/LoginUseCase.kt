package com.london.domain.usecase.login

import com.london.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class LoginUseCase(
    @Provided
    private val authRepository: AuthRepository
) {
    /**
     * Step 1: Get request token and launch WebView
     */
    operator fun invoke(username: String, password: String): Flow<Boolean> = flow {
        val result = authRepository.login(username, password)
    }

    /**
     * Step 2: Called after WebView success to create session
     */
//    fun createSession(): Flow<Boolean> = flow {
//        val result = authRepository.validateSession()
//    }
}
