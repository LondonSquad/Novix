package com.london.data.repository

import com.london.data.local.preference.AuthPreferences
import com.london.data.remote.model.authentication.model.DeleteSessionResponse
import com.london.data.remote.model.authentication.model.GuestSessionResponse
import com.london.data.remote.model.authentication.model.RequestTokenResponse
import com.london.data.remote.model.authentication.model.SessionResponse
import com.london.data.remote.source.authentication.AuthenticationRemoteDataSource
import com.london.data.repository.authentication.AuthenticationRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class AuthenticationRepositoryImplTest {

    private lateinit var repository: AuthenticationRepositoryImpl
    private val authRemoteDataSource: AuthenticationRemoteDataSource = mockk()
    private val authPreferences: AuthPreferences = mockk(relaxed = true)

    @Before
    fun setUp() {
        repository = AuthenticationRepositoryImpl(authRemoteDataSource, authPreferences)
    }

    // region: login()
    @Test
    fun `login returns true on success`() = runTest {
        val requestToken = "abc124"
        val sessionId = "sess456"

        coEvery { authRemoteDataSource.createRequestToken() } returns Result.success(
            RequestTokenResponse(true, "123", requestToken)
        )
        coEvery { authRemoteDataSource.createSessionWithLogin(any(), any(), any()) } returns Result.success(
            RequestTokenResponse(true, "123", requestToken)
        )
        coEvery { authRemoteDataSource.createSession(requestToken) } returns Result.success(
            SessionResponse(true, sessionId)
        )

        val result = repository.login("user", "pass")

        assertTrue(result)
        coVerify {
            authPreferences.saveSessionId(sessionId)
            authPreferences.saveUsername("user")
            authPreferences.saveRequestToken(requestToken)
            authPreferences.setGuestMode(false)
        }
    }

    @Test
    fun `login returns false on failed login`() = runTest {
        coEvery { authRemoteDataSource.createRequestToken() } returns Result.success(
            RequestTokenResponse(true, "123", "abc124")
        )
        coEvery { authRemoteDataSource.createSessionWithLogin(any(), any(), any()) } returns Result.success(
            RequestTokenResponse(false, "123", "abc124")
        )

        val result = repository.login("user", "wrong")

        assertFalse(result)
        coVerify(exactly = 0) { authPreferences.saveSessionId(any()) }
    }

    @Test
    fun `login throws exception on network error`() = runTest {
        val networkError = RuntimeException("network error")
        coEvery { authRemoteDataSource.createRequestToken() } returns Result.failure(networkError)

        assertThrows(RuntimeException::class.java) {
            runTest { repository.login("user", "pass") }
        }
    }

    @Test
    fun `login throws exception when createSession fails`() = runTest {
        val requestToken = "abc124"
        val error = RuntimeException("session creation failed")

        coEvery { authRemoteDataSource.createRequestToken() } returns Result.success(
            RequestTokenResponse(true, "123", requestToken)
        )
        coEvery { authRemoteDataSource.createSessionWithLogin(any(), any(), any()) } returns Result.success(
            RequestTokenResponse(true, "123", requestToken)
        )
        coEvery { authRemoteDataSource.createSession(requestToken) } returns Result.failure(error)

        assertThrows(RuntimeException::class.java) {
            runTest { repository.login("user", "pass") }
        }
    }
    // endregion

    // region: loginAsGuest()
    @Test
    fun `loginAsGuest returns true on success`() = runTest {
        coEvery { authRemoteDataSource.createGuestSession() } returns Result.success(
            GuestSessionResponse(true, "guest123", "")
        )

        val result = repository.loginAsGuest()

        assertTrue(result)
        coVerify {
            authPreferences.saveGuestSessionId("guest123")
            authPreferences.setGuestMode(true)
        }
    }

    @Test
    fun `loginAsGuest returns false on failure`() = runTest {
        coEvery { authRemoteDataSource.createGuestSession() } returns Result.success(
            GuestSessionResponse(false, "", "")
        )

        val result = repository.loginAsGuest()

        assertFalse(result)
    }

    @Test
    fun `loginAsGuest throws exception on network error`() = runTest {
        val networkError = RuntimeException("network error")
        coEvery { authRemoteDataSource.createGuestSession() } returns Result.failure(networkError)

        assertThrows(RuntimeException::class.java) {
            runTest { repository.loginAsGuest() }
        }
    }
    // endregion

    // region: logout()
    @Test
    fun `logout clears auth and returns true when session exists`() = runTest {
        every { authPreferences.getSessionId() } returns "sess123"
        every { authPreferences.isGuestMode() } returns false
        coEvery { authRemoteDataSource.deleteSession() } returns Result.success(
            DeleteSessionResponse(true)
        )

        val result = repository.logout()

        assertTrue(result)
        coVerify { authRemoteDataSource.deleteSession() }
        verify { authPreferences.clearAuth() }
    }

    @Test
    fun `logout still returns true when no session exists`() = runTest {
        every { authPreferences.getSessionId() } returns null

        val result = repository.logout()

        assertTrue(result)
        verify { authPreferences.clearAuth() }
    }

    @Test
    fun `logout throws exception on delete session failure but still clears preferences`() = runTest {
        every { authPreferences.getSessionId() } returns "sess"
        every { authPreferences.isGuestMode() } returns false
        val error = RuntimeException("delete failed")
        coEvery { authRemoteDataSource.deleteSession() } returns Result.failure(error)

        assertThrows(RuntimeException::class.java) {
            runTest { repository.logout() }
        }
    }

    @Test
    fun `logout skips delete session when in guest mode`() = runTest {
        every { authPreferences.getSessionId() } returns "sess123"
        every { authPreferences.isGuestMode() } returns true

        val result = repository.logout()

        assertTrue(result)
        coVerify(exactly = 0) { authRemoteDataSource.deleteSession() }
        verify { authPreferences.clearAuth() }
    }
    // endregion

    // region: isLoggedIn()
    @Test
    fun `isLoggedIn returns true when user is logged in`() = runTest {
        every { authPreferences.isLoggedIn() } returns true

        val result = repository.isLoggedIn()

        assertTrue(result)
    }

    @Test
    fun `isLoggedIn returns false when user is not logged in`() = runTest {
        every { authPreferences.isLoggedIn() } returns false

        val result = repository.isLoggedIn()

        assertFalse(result)
    }
    // endregion

    @Test
    fun `loginAsGuest sets guest mode to true when successful`() = runTest {
        coEvery { authRemoteDataSource.createGuestSession() } returns Result.success(
            GuestSessionResponse(true, "guest123", "")
        )

        val result = repository.loginAsGuest()

        assertTrue(result)
        verify {
            authPreferences.setGuestMode(true)
        }
    }

    @Test
    fun `logout deletes session when session exists and not in guest mode`() = runTest {
        every { authPreferences.getSessionId() } returns "sess123"
        every { authPreferences.isGuestMode() } returns false
        coEvery { authRemoteDataSource.deleteSession() } returns Result.success(
            DeleteSessionResponse(true)
        )

        val result = repository.logout()

        assertTrue(result)
        coVerify { authRemoteDataSource.deleteSession() }
    }
}