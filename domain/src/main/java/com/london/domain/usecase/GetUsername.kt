package com.london.domain.usecase

import com.london.domain.repository.AccountRepository
import javax.inject.Inject

class GetUsername @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): String = accountRepository.getUserName()
}