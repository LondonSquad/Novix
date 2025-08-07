package com.london.data.remote.source.account

import com.london.data.remote.model.account.AccountInfoResponse
import com.london.data.remote.service.account.AccountApiService
import com.london.data.remote.source.base.BaseRemoteDatasource

class AccountRemoteDataSourceImp(
    private val accountApiService: AccountApiService
) : AccountRemoteDataSource, BaseRemoteDatasource {
    override suspend fun getUserName(sessionId: String): Result<AccountInfoResponse> =
        callApi(
            apiCall = { accountApiService.getAccountInfo(sessionId) },
            mapper = { it }
        )
}