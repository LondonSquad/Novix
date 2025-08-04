package com.london.data.remote.source.authentication


import com.london.data.remote.model.authentication.DeleteSessionResponse
import com.london.data.remote.model.authentication.GuestSessionResponse
import com.london.data.remote.model.authentication.LoginValidationRequestBody
import com.london.data.remote.model.authentication.RequestTokenResponse
import com.london.data.remote.model.authentication.SessionResponse
import com.london.data.remote.model.authentication.Token
import com.london.data.remote.service.authentication.AuthenticationApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import javax.inject.Inject

class AuthenticationRemoteDataSourceImpl @Inject constructor(
    private val authApiService: AuthenticationApiService
) : AuthenticationRemoteDataSource, BaseRemoteDatasource {

    override suspend fun createRequestToken(): Result<RequestTokenResponse> =
        callApi(
            apiCall = { authApiService.createRequestToken() },
            mapper = { it }
        )

    override suspend fun createGuestSession(): Result<GuestSessionResponse> =
        callApi(
            apiCall = { authApiService.createGuestSession() },
            mapper = { it }
        )

    override suspend fun createSession(requestToken: String): Result<SessionResponse> =
        callApi(
            apiCall = { authApiService.createSession(Token(requestToken)) },
            mapper = { it }
        )

    override suspend fun createSessionWithLogin(
        username: String,
        password: String,
        requestToken: String
    ): Result<RequestTokenResponse> =
        callApi(
            apiCall = {
                authApiService.createSessionWithLogin(
                    LoginValidationRequestBody(
                        username = username,
                        password = password,
                        requestToken = requestToken
                    )
                )
            },
            mapper = { it }
        )

    override suspend fun deleteSession(): Result<DeleteSessionResponse> =
        callApi(
            apiCall = { authApiService.deleteSession() },
            mapper = { it }
        )
}