package com.london.data.datasource.remote.auth.api

import com.london.data.datasource.remote.auth.model.CreateSessionWithLoginRequest
import com.london.data.datasource.remote.auth.model.CreateSessionWithLoginResponse
import com.london.data.datasource.remote.auth.model.DeleteSessionResponse
import com.london.data.datasource.remote.auth.model.GuestSessionResponse
import com.london.data.datasource.remote.auth.model.RequestTokenResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @GET("authentication/token/new")
    suspend fun getRequestToken(): RequestTokenResponse

    @POST("authentication/token/validate_with_login")
    suspend fun validateWithLogin(@Body request: CreateSessionWithLoginRequest): CreateSessionWithLoginResponse

    @GET("authentication/guest_session/new")
    suspend fun createGuestSession(): GuestSessionResponse

    @DELETE("authentication/session")
    suspend fun deleteSession(): DeleteSessionResponse
}
