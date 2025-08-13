package com.london.domain.repository

import com.london.domain.entity.AccountInfo

interface AccountRepository {
    suspend fun getAccountDetails(): AccountInfo
    suspend fun getAccountId(): Int
}