package com.london.domain.usecase

import com.london.domain.entity.AccountInfo
import com.london.domain.repository.AccountRepository
import com.london.domain.repository.AuthRepository
import com.london.domain.usecase.authentication.AuthenticationUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetAccountDetailsTest {

    private lateinit var accountRepository: AccountRepository
    private lateinit var authenticationUseCase: AuthenticationUseCase
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        accountRepository = mockk()
        authenticationUseCase = AuthenticationUseCase(
            authRepository = authRepository,
            accountRepository = accountRepository
        )
    }

    @Test
    fun `return account info when repository returns valid account info`() = runTest {
        // Given
        val expectedAccountInfo = AccountInfo(id = 1, userName = "Mohamed", avatarPath = "/avatar.jpg")
        coEvery { accountRepository.getAccountDetails() } returns expectedAccountInfo

        // When
        val result = authenticationUseCase.getAccountDetails()

        // Then
        assertEquals(expectedAccountInfo, result)
        coVerify { accountRepository.getAccountDetails() }
    }

    @Test
    fun `return account info with empty username when repository returns account info with empty username`() = runTest {
        // Given
        val expectedAccountInfo = AccountInfo(id = 1, userName = "", avatarPath = "")
        coEvery { accountRepository.getAccountDetails() } returns expectedAccountInfo

        // When
        val result = authenticationUseCase.getAccountDetails()

        // Then
        assertEquals(expectedAccountInfo, result)
        coVerify { accountRepository.getAccountDetails() }
    }
}
