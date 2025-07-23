package com.london.data.repository

import com.london.data.datasource.common.AuthPreferences
import com.london.data.datasource.remote.auth.api.AuthApiService
import com.london.data.datasource.remote.auth.model.LoginValidationRequestBody
import com.london.data.datasource.remote.auth.model.Token
import com.london.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
                } else {
                    false
                }
            }.getOrElse { exception ->
                false
            }
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
                } else {
                    false
                }
            }.getOrElse { exception ->
                false
            }
        }
    }

    override suspend fun logout(): Boolean {
        return withContext(Dispatchers.IO) {
            val result = runCatching {
                if (authPreferences.getSessionId() != null && !authPreferences.getGuestMode()) {
                    authApiService.deleteSession()
                }
                true
            }
            authPreferences.clearAuth()

            result.map { it }
                .getOrElse { exception ->
                    false
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