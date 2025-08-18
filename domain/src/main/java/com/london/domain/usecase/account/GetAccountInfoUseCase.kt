package com.london.domain.usecase.account

import com.london.domain.entity.account.AccountInfo
import com.london.domain.repository.AccountRepository
import javax.inject.Inject

class GetAccountInfoUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend fun invoke(): AccountInfo = accountRepository.getAccountInfo()
}
