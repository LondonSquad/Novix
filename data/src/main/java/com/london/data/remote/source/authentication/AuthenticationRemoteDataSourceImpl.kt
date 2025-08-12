package com.london.data.remote.source.authentication


import com.london.data.remote.model.authentication.DeleteSessionResponse
import com.london.data.remote.model.authentication.GuestSessionResponse
import com.london.data.remote.model.authentication.LoginRequestResponse
import com.london.data.remote.model.authentication.RequestTokenResponse
import com.london.data.remote.model.authentication.SessionId
import com.london.data.remote.model.authentication.SessionResponse
import com.london.data.remote.model.authentication.TokenResponse
import com.london.data.remote.service.authentication.AuthenticationApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import javax.inject.Inject

class AuthenticationRemoteDataSourceImpl @Inject constructor(
    private val authenticationApiService: AuthenticationApiService
) : AuthenticationRemoteDataSource, BaseRemoteDatasource {

    override suspend fun createRequestToken(): Result<RequestTokenResponse> {
        return callApi(
            apiCall = { authenticationApiService.createRequestToken() },
            mapper = { it }
        )
    }

    override suspend fun createGuestSession(): Result<GuestSessionResponse> {
        return callApi(
            apiCall = { authenticationApiService.createGuestSession() },
            mapper = { it }
        )
    }

    override suspend fun createSession(requestToken: String): Result<SessionResponse> {
        return callApi(
            apiCall = { authenticationApiService.createSession(TokenResponse(requestToken)) },
            mapper = { it }
        )
    }

    override suspend fun validateLoginCredentials(
        username: String,
        password: String,
        requestToken: String
    ): Result<RequestTokenResponse> {
        return callApi(
            apiCall = {
                authenticationApiService.validateLoginCredentials(
                    LoginRequestResponse(
                        username = username,
                        password = password,
                        requestToken = requestToken
                    )
                )
            },
            mapper = { it }
        )
    }

    override suspend fun deleteSession(
        sessionId: String
    ): Result<DeleteSessionResponse> {
        return callApi(
            apiCall = { authenticationApiService.deleteSession(SessionId(sessionId)) },
            mapper = { it }
        )
    }
}