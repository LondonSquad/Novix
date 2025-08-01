package com.london.data.repository.authentication

import com.london.data.local.preference.AuthPreferences
import com.london.data.remote.source.authentication.AuthenticationRemoteDataSource
import com.london.domain.repository.AuthRepository
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthenticationRemoteDataSource,
    private val authPreferences: AuthPreferences
) : AuthRepository {
    override suspend fun login(username: String, password: String): Boolean {

        val tokenResponse = authRemoteDataSource.createRequestToken().getOrThrow()

        val sessionResponse = authRemoteDataSource.createSessionWithLogin(
            username = username,
            password = password,
            requestToken = tokenResponse.requestToken
        ).getOrThrow()

        return if (sessionResponse.success) {

            val createdSession = authRemoteDataSource.createSession(
                requestToken = sessionResponse.requestToken
            ).getOrThrow()

            authPreferences.saveSessionId(createdSession.sessionId)
            authPreferences.saveUsername(username)
            authPreferences.saveRequestToken(sessionResponse.requestToken)
            authPreferences.setGuestMode(false)
            true
        } else {
            false
        }
    }

    override suspend fun loginAsGuest(): Boolean {
        val guestResponse = authRemoteDataSource.createGuestSession().getOrThrow()
        return if (guestResponse.success) {
            authPreferences.saveGuestSessionId(guestResponse.guestSessionId)
            authPreferences.setGuestMode(true)
            true
        } else {
            false
        }
    }

    override suspend fun logout(): Boolean {
        if (authPreferences.getSessionId() != null && !authPreferences.isGuestMode()) {
            authRemoteDataSource.deleteSession().getOrThrow()
        }
        authPreferences.clearAuth()
        return true
    }

    override suspend fun isLoggedIn(): Boolean = authPreferences.isLoggedIn()
}