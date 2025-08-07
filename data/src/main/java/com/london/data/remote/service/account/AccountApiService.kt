package com.london.data.remote.service.account

import com.london.data.remote.model.account.AccountInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AccountApiService {
    @GET("3/account")
    suspend fun getUserName(
        @Query("session_id") sessionId: String
    ): Response<AccountInfoResponse>
}