package com.london.data.repository

import com.london.data.datasource.common.AuthPreferences
import com.london.data.datasource.remote.auth.api.AuthApiService
import com.london.data.datasource.remote.auth.model.GuestSessionResponse
import com.london.data.datasource.remote.auth.model.RequestTokenResponse
import com.london.data.datasource.remote.auth.model.SessionResponse
import com.london.data.datasource.remote.auth.model.Token
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class AuthRepositoryImplTest {

    private lateinit var repository: AuthRepositoryImpl
    private val authApiService: AuthApiService = mockk()
    private val authPreferences: AuthPreferences = mockk(relaxed = true)

    @Before
    fun setUp() {
        repository = AuthRepositoryImpl(authApiService, authPreferences)
    }

    // region: login()
    @Test
    fun `login returns true on success`() = runTest {
        val requestToken = "abc124"
        val sessionId = "sess456"

        coEvery { authApiService.createRequestToken() } returns RequestTokenResponse(
            true,
            "123",
            requestToken
        )
        coEvery { authApiService.createSessionWithLogin(any()) } returns RequestTokenResponse(
            true,
            "123",
            requestToken
        )
        coEvery { authApiService.createSession(Token(requestToken)) } returns SessionResponse(
            true,
            sessionId
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
        coEvery { authApiService.createRequestToken() } returns RequestTokenResponse(
            true,
            "123",
            "abc124"
        )
        coEvery { authApiService.createSessionWithLogin(any()) } returns RequestTokenResponse(
            false,
            "123",
            "abc124"
        )

        val result = repository.login("user", "wrong")

        assertFalse(result)
        coVerify(exactly = 0) { authPreferences.saveSessionId(any()) }
    }

    @Test
    fun `login returns false on exception`() = runTest {
        coEvery { authApiService.createRequestToken() } throws RuntimeException("network error")

        val result = repository.login("user", "pass")

        assertFalse(result)
    }
    // endregion

    // region: loginAsGuest()
    @Test
    fun `loginAsGuest returns true on success`() = runTest {
        coEvery { authApiService.createGuestSession() } returns GuestSessionResponse(
            true,
            "guest123",
            ""
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
        coEvery { authApiService.createGuestSession() } returns GuestSessionResponse(false, "", "")

        val result = repository.loginAsGuest()

        assertFalse(result)
    }

    @Test
    fun `loginAsGuest returns false on exception`() = runTest {
        coEvery { authApiService.createGuestSession() } throws RuntimeException("network")

        val result = repository.loginAsGuest()

        assertFalse(result)
    }
    // endregion

    @Test
    fun `logout clears auth and returns true when session exists`() = runTest {
        // Mock: session exists and not in guest mode
        every { authPreferences.getSessionId() } returns "sess123"
        every { authPreferences.isGuestMode() } returns false

        // Fix: properly mock unit function
        coJustRun { authApiService.deleteSession() }

        val result = repository.logout()

        assertTrue(result)

        // Verify expected calls
        coVerify { authApiService.deleteSession() }
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
    fun `logout returns false on failure`() = runTest {
        every { authPreferences.getSessionId() } returns "sess"
        every { authPreferences.isGuestMode() } returns false
        coEvery { authApiService.deleteSession() } throws RuntimeException()

        val result = repository.logout()

        assertFalse(result)
    }
    // endregion

    // region: isLoggedIn()
    @Test
    fun `isLoggedIn returns true or false based on preferences`() {
        every { authPreferences.isLoggedIn() } returns true
        assertTrue(repository.isLoggedIn())

        every { authPreferences.isLoggedIn() } returns false
        assertFalse(repository.isLoggedIn())
    }
    // endregion

    // region: validateSession()
    @Test
    fun `validateSession emits true when session or guest session exists`() = runTest {
        every { authPreferences.getSessionId() } returns null
        every { authPreferences.getGuestSessionId() } returns "guest"

        val result = repository.validateSession().first()

        assertTrue(result)
    }

    @Test
    fun `validateSession emits false when both session and guest are null`() = runTest {
        every { authPreferences.getSessionId() } returns null
        every { authPreferences.getGuestSessionId() } returns null

        val result = repository.validateSession().first()

        assertFalse(result)
    }

    @Test
    fun `loginAsGuest sets guest mode to true when successful`() = runTest {
        coEvery { authApiService.createGuestSession() } returns GuestSessionResponse(
            true,
            "",
            "guest123"
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
        coJustRun { authApiService.deleteSession() }

        val result = repository.logout()

        assertTrue(result)
        coVerify { authApiService.deleteSession() }
    }

    // endregion
}
