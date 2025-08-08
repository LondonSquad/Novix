package com.london.data.repository.authentication

import com.london.data.local.preference.AuthPreferences
import com.london.data.mapper.account.toEntity
import com.london.data.remote.source.account.AccountRemoteDataSource
import com.london.data.remote.source.authentication.AuthenticationRemoteDataSource
import com.london.domain.repository.AuthRepository
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthenticationRemoteDataSource,
    private val accountRemoteDataSource: AccountRemoteDataSource,
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

            val accountResult = accountRemoteDataSource.getAccountDetails(createdSession.sessionId)
            val accountInfo = accountResult.getOrThrow().toEntity()
            val accountId = accountInfo.id
            authPreferences.saveAccountId(accountId)
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
        val sessionId = authPreferences.getSessionId()
        if (sessionId != null && !authPreferences.isGuestMode()) {
            authRemoteDataSource.deleteSession(sessionId).getOrThrow()
        }
        authPreferences.clearAuth()
        return true
    }

    override suspend fun isLoggedIn(): Boolean = authPreferences.isLoggedIn()

    override suspend fun getAccountId(): Int {
        return authPreferences.getAccountId()
    }
}
