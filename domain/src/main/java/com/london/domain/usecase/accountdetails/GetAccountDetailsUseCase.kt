package com.london.domain.usecase.accountdetails

import com.london.domain.entity.AccountInfo
import com.london.domain.repository.AccountRepository
import javax.inject.Inject

class GetAccountDetailsUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend fun invoke(): AccountInfo = accountRepository.getAccountDetails()
}
