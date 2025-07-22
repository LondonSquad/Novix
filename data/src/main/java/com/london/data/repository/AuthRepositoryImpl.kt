package com.london.data.repository

import android.util.Log
import com.london.data.datasource.common.AuthPreferences
import com.london.data.datasource.remote.auth.api.AuthApi
import com.london.data.datasource.remote.auth.model.Token
import com.london.data.datasource.remote.auth.model.ValidateWithLoginRequestBody
import com.london.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single
import timber.log.Timber

@Single
class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val authPreferences: AuthPreferences
) : AuthRepository {

    override fun login(username: String, password: String): Flow<Boolean> = flow {
        // Step 1: Get request token
        val tokenResponse = authApi.createRequestToken()

        val loginRequestBody = ValidateWithLoginRequestBody(
            username = username,
            password = password,
            requestToken = tokenResponse.requestToken
        )

        // Step 2: Validate token with login
        // Step 3: Create session
        val sessionResponse = authApi.createSessionWithLogin(loginRequestBody)
        if (sessionResponse.success) {
            val authenticatedRequestToken = sessionResponse.requestToken
            val createdSession = authApi.createSession(
                Token(authenticatedRequestToken)
            )
            Timber.tag("AuthRepositoryImple").d("Created session: ${createdSession.sessionId}")
            authPreferences.saveSessionId(createdSession.sessionId)
            authPreferences.saveUsername(username)
        } else throw Exception("Failed to create session")
        authPreferences.saveRequestToken(sessionResponse.requestToken)
        authPreferences.setGuestMode(false)
        emit(true)
    }

    override fun loginAsGuest(): Flow<Boolean> = flow {
        val guestResponse = authApi.createGuestSession()
        val guestSessionId = guestResponse.guestSessionId
            ?: throw Exception("Failed to create guest session")

        Log.d("AuthRepositoryImplGuestID", "Guest session ID: $guestSessionId")
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