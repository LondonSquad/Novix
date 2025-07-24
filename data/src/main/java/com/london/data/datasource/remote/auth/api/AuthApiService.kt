package com.london.data.datasource.remote.auth.api

import com.london.data.datasource.remote.auth.model.DeleteSessionResponse
import com.london.data.datasource.remote.auth.model.GuestSessionResponse
import com.london.data.datasource.remote.auth.model.LoginValidationRequestBody
import com.london.data.datasource.remote.auth.model.RequestTokenResponse
import com.london.data.datasource.remote.auth.model.SessionResponse
import com.london.data.datasource.remote.auth.model.Token
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @GET("3/authentication/token/new")
    suspend fun createRequestToken(): RequestTokenResponse

    @GET("3/authentication/guest_session/new")
    suspend fun createGuestSession(): GuestSessionResponse

    @POST("3/authentication/session/new")
    suspend fun createSession(@Body requestBody: Token): SessionResponse

    @POST("3/authentication/token/validate_with_login")
    suspend fun createSessionWithLogin(@Body requestBody: LoginValidationRequestBody): RequestTokenResponse

    @DELETE("3/authentication/session")
    suspend fun deleteSession(): DeleteSessionResponse
}