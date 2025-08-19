package com.london.domain.usecase.account

import com.london.domain.entity.account.AccountInfo
import com.london.domain.repository.AccountRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetAccountInfoUseCaseTest {
    private lateinit var accountRepository: AccountRepository
    private lateinit var getAccountInfoUseCase: GetAccountInfoUseCase

    @Before
    fun setUp() {
        accountRepository = mockk()
        getAccountInfoUseCase = GetAccountInfoUseCase(
            accountRepository
        )
    }

    @Test
    fun `return account info when repository returns valid account info`() = runTest {
        // Given
        val expectedAccountInfo =
            AccountInfo(id = 1, userName = "Mohamed", avatarPath = "/avatar.jpg")
        coEvery { accountRepository.getAccountInfo() } returns expectedAccountInfo

        // When
        val result = getAccountInfoUseCase.invoke()

        // Then
        Assert.assertEquals(expectedAccountInfo, result)
        coVerify { accountRepository.getAccountInfo() }
    }

    @Test
    fun `return account info with empty username when repository returns account info with empty username`() =
        runTest {
            // Given
            val expectedAccountInfo = AccountInfo(id = 1, userName = "", avatarPath = "")
            coEvery { accountRepository.getAccountInfo() } returns expectedAccountInfo

            // When
            val result = getAccountInfoUseCase.invoke()

            // Then
            Assert.assertEquals(expectedAccountInfo, result)
            coVerify { accountRepository.getAccountInfo() }
        }
}