package com.london.data.repository.account

import com.google.common.truth.Truth.assertThat
import com.london.data.local.preference.AuthPreferences
import com.london.data.remote.model.account.AccountInfoResponse
import com.london.data.remote.source.account.AccountRemoteDataSource
import com.london.domain.entity.AccountInfo
import com.london.domain.repository.AccountRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AccountRepositoryImpTest {

    private lateinit var remoteDataSource: AccountRemoteDataSource
    private lateinit var authPreferences: AuthPreferences
    private lateinit var repository: AccountRepository

    @Before
    fun setUp() {
        remoteDataSource = mockk(relaxed = true)
        authPreferences = mockk(relaxed = true)
        repository = AccountRepositoryImp(remoteDataSource, authPreferences)
    }

    @Test
    fun `getUserName returns username when session exists and remote call succeeds`() = runTest {
        // Given
        val sessionId = "valid_session"
        val expectedUsername = "mohamed"
        val accountResponse = AccountInfoResponse(1, expectedUsername)

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getUserName(sessionId) } returns Result.success(accountResponse)

        // When
        val result = repository.getUserName()

        // Then
        assertThat(result).isEqualTo(expectedUsername)
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getUserName(sessionId) }
    }

    @Test
    fun `getUserName returns empty string when no session ID exists`() = runTest {
        // Given
        coEvery { authPreferences.getSessionId() } returns null

        // When
        val result = repository.getUserName()

        // Then
        assertThat(result).isEqualTo("")
        coVerify { authPreferences.getSessionId() }
        coVerify(exactly = 0) { remoteDataSource.getUserName(any()) }
    }

    @Test
    fun `getUserName returns empty string when session exists but username is null`() = runTest {
        // Given
        val sessionId = "valid_session"
        val accountResponse = AccountInfoResponse(1, null)

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getUserName(sessionId) } returns Result.success(accountResponse)

        // When
        val result = repository.getUserName()

        // Then
        assertThat(result).isEqualTo("")
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getUserName(sessionId) }
    }

    @Test
    fun `getUserName returns empty string when session exists but username is empty`() = runTest {
        // Given
        val sessionId = "valid_session"
        val accountResponse = AccountInfoResponse(1, "")
        val accountEntity = AccountInfo(1, "")

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getUserName(sessionId) } returns Result.success(accountResponse)

        // When
        val result = repository.getUserName()

        // Then
        assertThat(result).isEqualTo("")
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getUserName(sessionId) }
    }

    @Test
    fun `getUserName throws exception when remote call fails`() = runTest {
        // Given
        val sessionId = "valid_session"
        val expectedException = RuntimeException("Network error")

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getUserName(sessionId) } returns Result.failure(expectedException)

        // When & Then
        try {
            repository.getUserName()
            assert(false) { "Expected exception to be thrown" }
        } catch (e: Exception) {
            assertThat(e).isEqualTo(expectedException)
        }

        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getUserName(sessionId) }
    }

}