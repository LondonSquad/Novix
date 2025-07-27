package com.london.data.remote.source.authentication
import com.london.data.remote.model.authentication.model.GuestSessionResponse
import com.london.data.remote.model.authentication.model.RequestTokenResponse
import com.london.data.remote.model.authentication.model.SessionResponse
import com.london.data.remote.model.authentication.model.DeleteSessionResponse

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