package com.london.domain.repository

import com.london.domain.entity.account.AccountInfo

interface AccountRepository {
    suspend fun getAccountInfo(): AccountInfo
    suspend fun getAccountId(): Int
}