package com.london.data.remote.service.account

import com.london.data.remote.model.ApiConstants
import com.london.data.remote.model.account.AccountInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AccountApiService {
    @GET(ApiConstants.GET_ACCOUNT_DETAILS)
    suspend fun getAccountDetails(
        @Query("session_id") sessionId: String
    ): Response<AccountInfoResponse>
}
