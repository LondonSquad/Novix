package com.london.data.remote.service.authentication

import com.london.data.remote.model.authentication.DeleteSessionResponse
import com.london.data.remote.model.authentication.GuestSessionResponse
import com.london.data.remote.model.authentication.LoginValidationRequestBody
import com.london.data.remote.model.authentication.RequestTokenResponse
import com.london.data.remote.model.authentication.SessionResponse
import com.london.data.remote.model.authentication.Token
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST


interface AuthenticationApiService {
    @GET("3/authentication/token/new")
    suspend fun createRequestToken(): Response<RequestTokenResponse>

    @GET("3/authentication/guest_session/new")
    suspend fun createGuestSession(): Response<GuestSessionResponse>

    @POST("3/authentication/session/new")
    suspend fun createSession(@Body requestBody: Token): Response<SessionResponse>

    @POST("3/authentication/token/validate_with_login")
    suspend fun createSessionWithLogin(@Body requestBody: LoginValidationRequestBody): Response<RequestTokenResponse>

    @DELETE("3/authentication/session")
    suspend fun deleteSession(): Response<DeleteSessionResponse>
}