package com.london.data.repository

import com.london.data.local.preference.AuthPreferences
import com.london.data.remote.model.auth.model.LoginValidationRequestBody
import com.london.data.remote.model.auth.model.Token
import com.london.data.remote.service.auth.AuthApiService
import com.london.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

@Single
class AuthRepositoryImpl(
    private val authApiService: AuthApiService,
    private val authPreferences: AuthPreferences
) : AuthRepository {

    override suspend fun login(username: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            runCatching {
                val tokenResponse = authApiService.createRequestToken()
                val loginRequestBody = LoginValidationRequestBody(
                    username = username,
                    password = password,
                    requestToken = tokenResponse.requestToken
                )
                val sessionResponse = authApiService.createSessionWithLogin(loginRequestBody)

                if (sessionResponse.success) {
                    val authenticatedRequestToken = sessionResponse.requestToken
                    val createdSession = authApiService.createSession(
                        Token(authenticatedRequestToken)
                    )
                    authPreferences.saveSessionId(createdSession.sessionId)
                    authPreferences.saveUsername(username)
                    authPreferences.saveRequestToken(sessionResponse.requestToken)
                    authPreferences.setGuestMode(false)
                    true
                } else { false }
            }.getOrElse { false }
        }
    }

    override suspend fun loginAsGuest(): Boolean {
        return withContext(Dispatchers.IO) {
            runCatching {
                val guestResponse = authApiService.createGuestSession()
                if (guestResponse.success) {
                    authPreferences.saveGuestSessionId(guestResponse.guestSessionId)
                    authPreferences.setGuestMode(true)
                    true
                } else { false }
            }.getOrElse { false }
        }
    }

    override suspend fun logout(): Boolean {
        return withContext(Dispatchers.IO) {
            val result = runCatching {
                if (authPreferences.getSessionId() != null && !authPreferences.isGuestMode()) {
                    authApiService.deleteSession()
                }
                true
            }
            authPreferences.clearAuth()

            result.map { it }
                .getOrElse { false }
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return withContext(Dispatchers.IO) { authPreferences.isLoggedIn() }
    }
}