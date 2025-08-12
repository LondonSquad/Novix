package com.london.data.remote.service.authentication

import com.london.data.remote.model.ApiConstants.CREATE_NEW_GUEST_SESSION
import com.london.data.remote.model.ApiConstants.CREATE_NEW_SESSION
import com.london.data.remote.model.ApiConstants.CREATE_NEW_TOKEN
import com.london.data.remote.model.ApiConstants.CREATE_NEW_TOKEN_AFTER_LOGIN
import com.london.data.remote.model.ApiConstants.CREATE_SESSION
import com.london.data.remote.model.authentication.DeleteSessionResponse
import com.london.data.remote.model.authentication.GuestSessionResponse
import com.london.data.remote.model.authentication.LoginRequest
import com.london.data.remote.model.authentication.RequestToken
import com.london.data.remote.model.authentication.RequestTokenResponse
import com.london.data.remote.model.authentication.SessionId
import com.london.data.remote.model.authentication.SessionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST

interface AuthenticationApiService {
    @GET(CREATE_NEW_TOKEN)
    suspend fun createRequestToken(): Response<RequestTokenResponse>

    @GET(CREATE_NEW_GUEST_SESSION)
    suspend fun createGuestSession(): Response<GuestSessionResponse>

    @POST(CREATE_NEW_SESSION)
    suspend fun createSession(
        @Body token: RequestToken
    ): Response<SessionResponse>

    @HTTP(method = "DELETE", path = CREATE_SESSION, hasBody = true)
    suspend fun deleteSession(
        @Body sessionId: SessionId
    ): Response<DeleteSessionResponse>

    @POST(CREATE_NEW_TOKEN_AFTER_LOGIN)
    suspend fun validateLoginCredentials(
        @Body request: LoginRequest
    ): Response<RequestTokenResponse>
}