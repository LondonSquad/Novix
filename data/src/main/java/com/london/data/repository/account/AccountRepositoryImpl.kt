package com.london.data.repository.account

import com.london.data.local.preference.AuthenticationPreferences
import com.london.data.mapper.account.toEntity
import com.london.data.remote.source.account.AccountRemoteDataSource
import com.london.domain.entity.account.AccountInfo
import com.london.domain.repository.AccountRepository
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountRemoteDataSource: AccountRemoteDataSource,
    private val authenticationPreferences: AuthenticationPreferences
) : AccountRepository {

    override suspend fun getAccountInfo(): AccountInfo =
        authenticationPreferences.getSessionId()?.let { sessionId ->
            accountRemoteDataSource.getAccountInfo(sessionId)
                .getOrThrow()
                .toEntity()
        } ?: AccountInfo(id = 0, userName = "", avatarPath = "")

    override suspend fun getAccountId(): Int = authenticationPreferences.getAccountId()

}
