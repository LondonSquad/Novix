package com.london.domain.usecase

import com.london.domain.entity.AccountInfo
import com.london.domain.repository.AccountRepository
import javax.inject.Inject

class GetAccountDetails @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): AccountInfo = accountRepository.getAccountDetails()
}