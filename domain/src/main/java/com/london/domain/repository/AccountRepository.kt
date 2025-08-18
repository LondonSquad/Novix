package com.london.domain.repository

import com.london.domain.entity.account.AccountInfo

interface AccountRepository {
    suspend fun getAccountDetails(): AccountInfo
    suspend fun getAccountId(): Int
}