package com.london.data.remote.source.authentication
import com.london.data.remote.model.authentication.DeleteSessionResponse
import com.london.data.remote.model.authentication.GuestSessionResponse
import com.london.data.remote.model.authentication.RequestTokenResponse
import com.london.data.remote.model.authentication.SessionResponse

interface AuthenticationRemoteDataSource {
    suspend fun createRequestToken(): Result<RequestTokenResponse>
    suspend fun createGuestSession(): Result<GuestSessionResponse>
    suspend fun createSession(requestToken: String): Result<SessionResponse>
    suspend fun createSessionWithLogin(
        username: String,
        password: String,
        requestToken: String
    ): Result<RequestTokenResponse>
    suspend fun deleteSession(): Result<DeleteSessionResponse>
}