package com.london.data.repository.account

import com.london.data.local.preference.AuthPreferences
import com.london.data.mapper.account.toEntity
import com.london.data.remote.source.account.AccountRemoteDataSource
import com.london.domain.entity.AccountInfo
import com.london.domain.repository.AccountRepository
import javax.inject.Inject

class AccountRepositoryImp @Inject constructor(
    private val accountRemoteDataSource: AccountRemoteDataSource,
    private val authPreferences: AuthPreferences
) : AccountRepository {
    override suspend fun getAccountDetails(): AccountInfo =
        authPreferences.getSessionId()?.let { sessionId ->
            accountRemoteDataSource.getAccountDetails(sessionId)
                .getOrThrow()
                .toEntity()
        } ?: AccountInfo(id = 0, userName = "", avatarPath = "")
}
