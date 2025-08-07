package com.london.data.repository.account

import com.google.common.truth.Truth.assertThat
import com.london.data.local.preference.AuthPreferences
import com.london.data.remote.model.account.AccountInfoResponse
import com.london.data.remote.source.account.AccountRemoteDataSource
import com.london.domain.repository.AccountRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import com.london.data.remote.model.account.AvatarInfo
import com.london.data.remote.model.account.AvatarDetails

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
    fun `getAccountInfo returns account info when session exists and remote call succeeds`() = runTest {
        // Given
        val sessionId = "valid_session"
        val expectedUsername = "mohamed"
        val accountResponse = AccountInfoResponse(
            id = 1, 
            userName = expectedUsername, 
            name = expectedUsername,
            avatar = null
        )

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getAccountDetails(sessionId) } returns Result.success(accountResponse)

        // When
        val result = repository.getAccountDetails()

        // Then
        assertThat(result.userName).isEqualTo(expectedUsername)
        assertThat(result.id).isEqualTo(1)
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getAccountDetails(sessionId) }
    }

    @Test
    fun `getAccountInfo returns default account info when no session ID exists`() = runTest {
        // Given
        coEvery { authPreferences.getSessionId() } returns null

        // When
        val result = repository.getAccountDetails()

        // Then
        assertThat(result.userName).isEqualTo("")
        assertThat(result.id).isEqualTo(0)
        assertThat(result.avatarPath).isEqualTo("")
        coVerify { authPreferences.getSessionId() }
        coVerify(exactly = 0) { remoteDataSource.getAccountDetails(any()) }
    }

    @Test
    fun `getAccountInfo returns account info with empty username when session exists but username is null`() = runTest {
        // Given
        val sessionId = "valid_session"
        val accountResponse = AccountInfoResponse(id = 1, userName = null, name = null, avatar = null)

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getAccountDetails(sessionId) } returns Result.success(accountResponse)

        // When
        val result = repository.getAccountDetails()

        // Then
        assertThat(result.userName).isEqualTo("")
        assertThat(result.id).isEqualTo(1)
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getAccountDetails(sessionId) }
    }

    @Test
    fun `getAccountInfo returns account info with empty username when session exists but username is empty`() = runTest {
        // Given
        val sessionId = "valid_session"
        val accountResponse = AccountInfoResponse(id = 1, userName = "", name = "", avatar = null)

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getAccountDetails(sessionId) } returns Result.success(accountResponse)

        // When
        val result = repository.getAccountDetails()

        // Then
        assertThat(result.userName).isEqualTo("")
        assertThat(result.id).isEqualTo(1)
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getAccountDetails(sessionId) }
    }

    @Test
    fun `getAccountInfo throws exception when remote call fails`() = runTest {
        // Given
        val sessionId = "valid_session"
        val expectedException = RuntimeException("Network error")

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getAccountDetails(sessionId) } returns Result.failure(expectedException)

        // When & Then
        try {
            repository.getAccountDetails()
            assert(false) { "Expected exception to be thrown" }
        } catch (e: Exception) {
            assertThat(e).isEqualTo(expectedException)
        }

        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getAccountDetails(sessionId) }
    }

    @Test
    fun `getAccountDetails returns full account info when session exists and remote call succeeds`() = runTest {
        // Given
        val sessionId = "valid_session_id_123"
        val expectedUsername = "john_doe"
        val expectedName = "John Doe"
        val expectedAvatarPath = "/path/to/avatar.jpg"
        
        val accountResponse = AccountInfoResponse(
            id = 12345, 
            userName = expectedUsername, 
            name = expectedName,
            avatar = AvatarInfo(
                tmdb = AvatarDetails(avatarPath = expectedAvatarPath)
            )
        )

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getAccountDetails(sessionId) } returns Result.success(accountResponse)

        // When
        val result = repository.getAccountDetails()

        // Then
        assertThat(result.id).isEqualTo(12345)
        assertThat(result.userName).isEqualTo(expectedName) // Should use name since it's not blank
        assertThat(result.avatarPath).isEqualTo("https://image.tmdb.org/t/p/w500/path/to/avatar.jpg")
        
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getAccountDetails(sessionId) }
    }
}