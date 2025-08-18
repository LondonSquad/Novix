package com.london.data.remote.source.account

import com.london.data.remote.model.account.AccountInfoResponse
import com.london.data.remote.service.account.AccountApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import javax.inject.Inject

class AccountRemoteDataSourceImp @Inject constructor(
    private val accountApiService: AccountApiService
) : AccountRemoteDataSource, BaseRemoteDatasource {
    override suspend fun getAccountDetails(sessionId: String): Result<AccountInfoResponse> =
        callApi(
            apiCall = { accountApiService.getAccountDetails(sessionId) },
            mapper = { it }
        )
}