package com.london.data.repository.account

import com.london.data.local.preference.AuthPreferences
import com.london.data.mapper.account.toEntity
import com.london.data.remote.source.account.AccountRemoteDataSource
import com.london.domain.repository.AccountRepository
import javax.inject.Inject

class AccountRepositoryImp @Inject constructor(
    private val accountRemoteDataSource: AccountRemoteDataSource,
    private val authPreferences: AuthPreferences
) : AccountRepository {
    override suspend fun getUserName(): String =
        authPreferences.getSessionId()?.let { sessionId ->
            accountRemoteDataSource.getUserName(sessionId)
                .getOrThrow()
                .toEntity()
                .userName
        } ?: ""
}
