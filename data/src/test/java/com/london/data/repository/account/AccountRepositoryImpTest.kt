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
        assertThat(result.userName).isEqualTo(expectedName)
        assertThat(result.avatarPath).isEqualTo("https://image.tmdb.org/t/p/w500/path/to/avatar.jpg")
        
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getAccountDetails(sessionId) }
    }

    @Test
    fun `getAccountDetails handles account with null id`() = runTest {
        // Given
        val sessionId = "valid_session"
        val accountResponse = AccountInfoResponse(
            id = null, 
            userName = "testuser", 
            name = "Test User",
            avatar = null
        )

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getAccountDetails(sessionId) } returns Result.success(accountResponse)

        // When
        val result = repository.getAccountDetails()

        // Then
        assertThat(result.id).isEqualTo(0)
        assertThat(result.userName).isEqualTo("Test User")
        assertThat(result.avatarPath).isEqualTo("")
        
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getAccountDetails(sessionId) }
    }

    @Test
    fun `getAccountDetails handles account with blank name`() = runTest {
        // Given
        val sessionId = "valid_session"
        val accountResponse = AccountInfoResponse(
            id = 1, 
            userName = "testuser", 
            name = "   ", // Blank name
            avatar = null
        )

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getAccountDetails(sessionId) } returns Result.success(accountResponse)

        // When
        val result = repository.getAccountDetails()

        // Then
        assertThat(result.id).isEqualTo(1)
        assertThat(result.userName).isEqualTo("testuser")
        assertThat(result.avatarPath).isEqualTo("")
        
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getAccountDetails(sessionId) }
    }

    @Test
    fun `getAccountDetails handles account with empty name`() = runTest {
        // Given
        val sessionId = "valid_session"
        val accountResponse = AccountInfoResponse(
            id = 1, 
            userName = "testuser", 
            name = "", // Empty name
            avatar = null
        )

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getAccountDetails(sessionId) } returns Result.success(accountResponse)

        // When
        val result = repository.getAccountDetails()

        // Then
        assertThat(result.id).isEqualTo(1)
        assertThat(result.userName).isEqualTo("testuser")
        assertThat(result.avatarPath).isEqualTo("")
        
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getAccountDetails(sessionId) }
    }

    @Test
    fun `getAccountDetails handles account with avatar but null tmdb`() = runTest {
        // Given
        val sessionId = "valid_session"
        val accountResponse = AccountInfoResponse(
            id = 1, 
            userName = "testuser", 
            name = "Test User",
            avatar = AvatarInfo(tmdb = null)
        )

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getAccountDetails(sessionId) } returns Result.success(accountResponse)

        // When
        val result = repository.getAccountDetails()

        // Then
        assertThat(result.id).isEqualTo(1)
        assertThat(result.userName).isEqualTo("Test User")
        assertThat(result.avatarPath).isEqualTo("")
        
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getAccountDetails(sessionId) }
    }

    @Test
    fun `getAccountDetails handles account with avatar but null avatarPath`() = runTest {
        // Given
        val sessionId = "valid_session"
        val accountResponse = AccountInfoResponse(
            id = 1, 
            userName = "testuser", 
            name = "Test User",
            avatar = AvatarInfo(
                tmdb = AvatarDetails(avatarPath = null)
            )
        )

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getAccountDetails(sessionId) } returns Result.success(accountResponse)

        // When
        val result = repository.getAccountDetails()

        // Then
        assertThat(result.id).isEqualTo(1)
        assertThat(result.userName).isEqualTo("Test User")
        assertThat(result.avatarPath).isEqualTo("")
        
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getAccountDetails(sessionId) }
    }

    @Test
    fun `getAccountDetails handles account with empty avatarPath`() = runTest {
        // Given
        val sessionId = "valid_session"
        val accountResponse = AccountInfoResponse(
            id = 1, 
            userName = "testuser", 
            name = "Test User",
            avatar = AvatarInfo(
                tmdb = AvatarDetails(avatarPath = "")
            )
        )

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getAccountDetails(sessionId) } returns Result.success(accountResponse)

        // When
        val result = repository.getAccountDetails()

        // Then
        assertThat(result.id).isEqualTo(1)
        assertThat(result.userName).isEqualTo("Test User")
        assertThat(result.avatarPath).isEqualTo("https://image.tmdb.org/t/p/w500")
        
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getAccountDetails(sessionId) }
    }

    @Test
    fun `getAccountDetails handles account with null userName`() = runTest {
        // Given
        val sessionId = "valid_session"
        val accountResponse = AccountInfoResponse(
            id = 1, 
            userName = null, 
            name = "Test User",
            avatar = null
        )

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getAccountDetails(sessionId) } returns Result.success(accountResponse)

        // When
        val result = repository.getAccountDetails()

        // Then
        assertThat(result.id).isEqualTo(1)
        assertThat(result.userName).isEqualTo("Test User")
        assertThat(result.avatarPath).isEqualTo("")
        
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getAccountDetails(sessionId) }
    }

    @Test
    fun `getAccountDetails handles account with empty userName`() = runTest {
        // Given
        val sessionId = "valid_session"
        val accountResponse = AccountInfoResponse(
            id = 1, 
            userName = "", 
            name = "Test User",
            avatar = null
        )

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getAccountDetails(sessionId) } returns Result.success(accountResponse)

        // When
        val result = repository.getAccountDetails()

        // Then
        assertThat(result.id).isEqualTo(1)
        assertThat(result.userName).isEqualTo("Test User")
        assertThat(result.avatarPath).isEqualTo("")
        
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getAccountDetails(sessionId) }
    }

    @Test
    fun `getAccountDetails handles account with both name and userName as null`() = runTest {
        // Given
        val sessionId = "valid_session"
        val accountResponse = AccountInfoResponse(
            id = 1, 
            userName = null, 
            name = null,
            avatar = null
        )

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getAccountDetails(sessionId) } returns Result.success(accountResponse)

        // When
        val result = repository.getAccountDetails()

        // Then
        assertThat(result.id).isEqualTo(1)
        assertThat(result.userName).isEqualTo("")
        assertThat(result.avatarPath).isEqualTo("")
        
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getAccountDetails(sessionId) }
    }

    @Test
    fun `getAccountDetails handles account with both name and userName as empty`() = runTest {
        // Given
        val sessionId = "valid_session"
        val accountResponse = AccountInfoResponse(
            id = 1, 
            userName = "", 
            name = "",
            avatar = null
        )

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getAccountDetails(sessionId) } returns Result.success(accountResponse)

        // When
        val result = repository.getAccountDetails()

        // Then
        assertThat(result.id).isEqualTo(1)
        assertThat(result.userName).isEqualTo("")
        assertThat(result.avatarPath).isEqualTo("")
        
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getAccountDetails(sessionId) }
    }

    @Test
    fun `getAccountDetails handles account with zero id`() = runTest {
        // Given
        val sessionId = "valid_session"
        val accountResponse = AccountInfoResponse(
            id = 0, 
            userName = "testuser", 
            name = "Test User",
            avatar = null
        )

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getAccountDetails(sessionId) } returns Result.success(accountResponse)

        // When
        val result = repository.getAccountDetails()

        // Then
        assertThat(result.id).isEqualTo(0)
        assertThat(result.userName).isEqualTo("Test User")
        assertThat(result.avatarPath).isEqualTo("")
        
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getAccountDetails(sessionId) }
    }

    @Test
    fun `getAccountDetails covers let block when sessionId is not null`() = runTest {
        // Given
        val sessionId = "test_session_id"
        val accountResponse = AccountInfoResponse(
            id = 1, 
            userName = "testuser", 
            name = "Test User",
            avatar = null
        )

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getAccountDetails(sessionId) } returns Result.success(accountResponse)

        // When
        val result = repository.getAccountDetails()

        // Then
        assertThat(result.id).isEqualTo(1)
        assertThat(result.userName).isEqualTo("Test User")
        assertThat(result.avatarPath).isEqualTo("")
        
        // Verify
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getAccountDetails(sessionId) }
    }

    @Test
    fun `getAccountDetails covers let block when sessionId is null`() = runTest {
        // Given
        coEvery { authPreferences.getSessionId() } returns null

        // When
        val result = repository.getAccountDetails()

        // Then
        assertThat(result.id).isEqualTo(0)
        assertThat(result.userName).isEqualTo("")
        assertThat(result.avatarPath).isEqualTo("")
        
        // Verify
        coVerify(exactly = 0) { remoteDataSource.getAccountDetails(any()) }
    }

    @Test
    fun `getAccountDetails covers let block with empty sessionId`() = runTest {
        // Given
        val sessionId = ""
        val accountResponse = AccountInfoResponse(
            id = 1, 
            userName = "testuser", 
            name = "Test User",
            avatar = null
        )

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { remoteDataSource.getAccountDetails(sessionId) } returns Result.success(accountResponse)

        // When
        val result = repository.getAccountDetails()

        // Then
        assertThat(result.id).isEqualTo(1)
        assertThat(result.userName).isEqualTo("Test User")
        assertThat(result.avatarPath).isEqualTo("")
        
        // Verify
        coVerify { authPreferences.getSessionId() }
        coVerify { remoteDataSource.getAccountDetails(sessionId) }
    }
}