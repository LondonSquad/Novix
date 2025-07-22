package com.london.data.datasource.remote.auth.api

import com.london.data.datasource.remote.auth.model.DeleteSessionResponse
import com.london.data.datasource.remote.auth.model.GuestSessionResponse
import com.london.data.datasource.remote.auth.model.RequestTokenResponse
import com.london.data.datasource.remote.auth.model.SessionResponse
import com.london.data.datasource.remote.auth.model.Token
import com.london.data.datasource.remote.auth.model.ValidateKeyResponse
import com.london.data.datasource.remote.auth.model.ValidateWithLoginRequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @GET("3/authentication/guest_session/new")
    suspend fun createGuestSession(): GuestSessionResponse

    @GET("3/authentication/token/new")
    suspend fun createRequestToken(): RequestTokenResponse

    @POST("3/authentication/session/new")
    suspend fun createSession(@Body requestBody: Token): SessionResponse

    @POST("3/authentication/token/validate_with_login")
    suspend fun createSessionWithLogin(@Body requestBody: ValidateWithLoginRequestBody): RequestTokenResponse

    @DELETE("3/authentication/session")
    suspend fun deleteSession(): DeleteSessionResponse

    @GET("3/authentication")
    suspend fun validateKey(): ValidateKeyResponse
}