package com.london.domain.usecase

import com.london.domain.repository.AccountRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetUsernameTest {

    private lateinit var accountRepository: AccountRepository
    private lateinit var getUsername: GetUsername

    @Before
    fun setUp() {
        accountRepository = mockk()
        getUsername = GetUsername(accountRepository)
    }

    @Test
    fun `return username when repository returns valid username`() = runTest {
        // Given
        val expectedUsername = "Mohamed"
        coEvery { accountRepository.getUserName() } returns expectedUsername

        // When
        val result = getUsername()

        // Then
        assertEquals(expectedUsername, result)
        coVerify { accountRepository.getUserName() }
    }

    @Test
    fun `return empty string when repository returns empty username`() = runTest {
        // Given
        coEvery { accountRepository.getUserName() } returns ""

        // When
        val result = getUsername()

        // Then
        assertEquals("", result)
        coVerify { accountRepository.getUserName() }
    }
}
