package com.london.data.repository

import com.london.data.datasource.common.AuthPreferences
import com.london.data.datasource.remote.auth.api.AuthApi
import com.london.data.datasource.remote.auth.model.Token
import com.london.data.datasource.remote.auth.model.ValidateWithLoginRequestBody
import com.london.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

@Single
class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val authPreferences: AuthPreferences
) : AuthRepository {

    override suspend fun login(username: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val tokenResponse = authApi.createRequestToken()

                val loginRequestBody = ValidateWithLoginRequestBody(
                    username = username,
                    password = password,
                    requestToken = tokenResponse.requestToken
                )

                val sessionResponse = authApi.createSessionWithLogin(loginRequestBody)

                if (sessionResponse.success) {
                    val authenticatedRequestToken = sessionResponse.requestToken
                    val createdSession = authApi.createSession(
                        Token(authenticatedRequestToken)
                    )
                    authPreferences.saveSessionId(createdSession.sessionId)
                    authPreferences.saveUsername(username)
                    authPreferences.saveRequestToken(sessionResponse.requestToken)
                    authPreferences.setGuestMode(false)
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun loginAsGuest(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val guestResponse = authApi.createGuestSession()
                val guestSessionId = guestResponse.guestSessionId

                if (guestResponse.success) {
                    authPreferences.saveGuestSessionId(guestSessionId)
                    authPreferences.setGuestMode(true)
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun logout(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val sessionId = authPreferences.getSessionId()
                if (sessionId != null && !authPreferences.isGuestMode()) {
                    authApi.deleteSession()
                }
                true
            } catch (e: Exception) {
                false
            } finally {
                authPreferences.clearAuth()
            }
        }
    }

    override fun isLoggedIn(): Boolean {
        return authPreferences.isLoggedIn()
    }

    override fun validateSession(): Flow<Boolean> = flow {
        val sessionId = authPreferences.getSessionId()
        val guestSessionId = authPreferences.getGuestSessionId()
        val isValid = sessionId != null || guestSessionId != null
        emit(isValid)
    }.flowOn(Dispatchers.IO)
}