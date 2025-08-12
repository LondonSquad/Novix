package com.london.data.repository.authentication

import com.london.data.local.preference.AuthenticationPreferences
import com.london.data.mapper.account.toEntity
import com.london.data.remote.model.authentication.RequestTokenResponse
import com.london.data.remote.model.authentication.SessionResponse
import com.london.data.remote.source.account.AccountRemoteDataSource
import com.london.data.remote.source.authentication.AuthenticationRemoteDataSource
import com.london.domain.repository.AuthenticationRepository
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val accountRemoteDataSource: AccountRemoteDataSource,
    private val authenticationPreferences: AuthenticationPreferences
) : AuthenticationRepository {
    override suspend fun login(username: String, password: String): Boolean {

        val sessionResponse = attemptLogin(
            username = username,
            password = password
        )

        if (!sessionResponse.success) return false

        val createdSession = createSession(sessionResponse)
        saveUserSession(
            username = username,
            session = createdSession,
            requestToken = sessionResponse.requestToken
        )

        getUserAccount(session = createdSession)
        return true
    }

    override suspend fun loginAsGuest(): Boolean {
        val guestResponse = authenticationRemoteDataSource.createGuestSession().getOrThrow()
        if (!guestResponse.success) return false

        authenticationPreferences.apply {
            saveGuestSessionId(guestResponse.guestSessionId)
            setGuestMode(true)
        }
        return true
    }

    override suspend fun logout(): Boolean {
        val sessionId = authenticationPreferences.getSessionId()
        if (sessionId != null && !authenticationPreferences.isGuestMode()) {
            authenticationRemoteDataSource.deleteSession(sessionId).getOrThrow()
        }
        authenticationPreferences.clearAuthentication()
        return true
    }

    override suspend fun isLoggedIn(): Boolean = authenticationPreferences.isLoggedIn()

    private suspend fun fetchRequestToken(): RequestTokenResponse {
        return authenticationRemoteDataSource.createRequestToken().getOrThrow()
    }

    private suspend fun attemptLogin(
        username: String,
        password: String
    ): RequestTokenResponse {
        return authenticationRemoteDataSource.validateLoginCredentials(
            username = username,
            password = password,
            requestToken = fetchRequestToken().requestToken
        ).getOrThrow()
    }

    private suspend fun createSession(session: RequestTokenResponse): SessionResponse {
        return authenticationRemoteDataSource.createSession(
            requestToken = session.requestToken
        ).getOrThrow()
    }

    private fun saveUserSession(
        username: String,
        session: SessionResponse,
        requestToken: String
    ) {
        authenticationPreferences.apply {
            saveSessionId(session.sessionId)
            saveUsername(username)
            saveRequestToken(requestToken)
            setGuestMode(false)
        }
    }

    private suspend fun getUserAccount(session: SessionResponse) {
        val accountResult = accountRemoteDataSource.getAccountDetails(session.sessionId)
        val accountInfo = accountResult.getOrThrow().toEntity()
        val accountId = accountInfo.id
        saveUserAccount(id = accountId)
    }

    private fun saveUserAccount(id: Int) {
        authenticationPreferences.saveAccountId(id)
    }
}
