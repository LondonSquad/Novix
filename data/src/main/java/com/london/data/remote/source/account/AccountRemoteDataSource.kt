package com.london.data.remote.source.account

import com.london.data.remote.model.account.AccountInfoResponse

interface AccountRemoteDataSource {
    suspend fun getAccountInfo(sessionId: String): Result<AccountInfoResponse>
}
