package com.london.data.datasource.remote.auth

import com.london.data.datasource.remote.auth.model.CreateSessionRequest
import com.london.data.datasource.remote.auth.model.DeleteSessionResponse
import com.london.data.datasource.remote.auth.model.GuestSessionResponse
import com.london.data.datasource.remote.auth.model.LoginResponse
import com.london.data.datasource.remote.auth.model.RequestTokenResponse
import com.london.data.datasource.remote.auth.model.SessionResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @GET("authentication/token/new")
    suspend fun getRequestToken(): RequestTokenResponse

    @POST("authentication/token/validate_with_login")
    suspend fun loginSession(@Body body: LoginResponse): RequestTokenResponse

    @POST("3/authentication/session/new")
    suspend fun createSession(@Body body: CreateSessionRequest): SessionResponse

    @GET("authentication/guest_session/new")
    suspend fun createGuestSession(): GuestSessionResponse

    @DELETE("authentication/session")
    suspend fun deleteSession(): DeleteSessionResponse
}
