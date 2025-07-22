package com.london.data.repository

import com.london.data.datasource.common.AuthPreferences
import com.london.data.datasource.remote.auth.api.AuthApi
import com.london.data.datasource.remote.auth.model.CreateSessionWithLoginRequest
import com.london.data.datasource.remote.auth.model.RequestTokenResponse
import com.london.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val authPreferences: AuthPreferences
) : AuthRepository {

    override fun login(username: String, password: String): Flow<Boolean> = flow {
        // Step 1: Get request token
        val tokenResponse = authApi.getRequestToken()
        val requestToken = tokenResponse.requestToken
            ?: throw Exception("Failed to get request token")

        authPreferences.saveRequestToken(requestToken)

        // Step 2: Validate token with login
        val loginRequest = CreateSessionWithLoginRequest(
            username = username,
            password = password,
            requestToken = requestToken
        )

        val isValidated = authApi.validateWithLogin(loginRequest)
        if (!isValidated) throw Exception("Invalid username or password")

        // Step 3: Create session
        val sessionResponse = authApi.createSession(
            RequestTokenResponse(
                success = true,
                expiresAt = null,
                requestToken = requestToken
            )
        )

        val sessionId = sessionResponse.sessionId
        if (!sessionResponse.success || sessionId.isNullOrEmpty()) {
            throw Exception("Failed to create session")
        }

        // Save session data
        authPreferences.saveSessionId(sessionId)
        authPreferences.saveUsername(username)
        authPreferences.setGuestMode(false)

        emit(true)
    }

    override fun loginAsGuest(): Flow<Boolean> = flow {
        val guestResponse = authApi.createGuestSession()
        val guestSessionId = guestResponse.guestSessionId
            ?: throw Exception("Failed to create guest session")

        authPreferences.saveGuestSessionId(guestSessionId)
        authPreferences.setGuestMode(true)

        emit(true)
    }

    override fun logout(): Flow<Boolean> = flow {
        try {
            val sessionId = authPreferences.getSessionId()
            if (sessionId != null && !authPreferences.isGuestMode()) {
                authApi.deleteSession()
            }
        } finally {
            authPreferences.clearAuth()
            emit(true)
        }
    }

    override fun isLoggedIn(): Boolean {
        return authPreferences.isLoggedIn()
    }

    override fun validateSession(): Flow<Boolean> = flow {
        val sessionId = authPreferences.getSessionId()
        val guestSessionId = authPreferences.getGuestSessionId()
        emit(sessionId != null || guestSessionId != null)
    }
}
