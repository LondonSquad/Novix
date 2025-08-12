package com.london.data.repository

import com.london.data.local.preference.AuthenticationPreferences
import com.london.data.remote.model.account.AccountInfoResponse
import com.london.data.remote.model.authentication.DeleteSessionResponse
import com.london.data.remote.model.authentication.GuestSessionResponse
import com.london.data.remote.model.authentication.RequestTokenResponse
import com.london.data.remote.model.authentication.SessionResponse
import com.london.data.remote.source.account.AccountRemoteDataSource
import com.london.data.remote.source.authentication.AuthenticationRemoteDataSource
import com.london.data.repository.authentication.AuthenticationRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthenticationRepositoryImplTest {

    private lateinit var repository: AuthenticationRepositoryImpl
    private val authRemoteDataSource: AuthenticationRemoteDataSource = mockk()
    private val accountRemoteDataSource: AccountRemoteDataSource = mockk()
    private val authenticationPreferences: AuthenticationPreferences = mockk(relaxed = true)


    @Before
    fun setUp() {
        repository = AuthenticationRepositoryImpl(
            authRemoteDataSource,
            accountRemoteDataSource,
            authenticationPreferences
        )
    }

    // region: login()
    @Test
    fun `login returns true on success and fetches account details`() = runTest {
        coEvery { authRemoteDataSource.createRequestToken() } returns Result.success(
            RequestTokenResponse(true, EXPIRES_AT, REQUEST_TOKEN)
        )
        coEvery {
            authRemoteDataSource.validateLoginCredentials(
                any(),
                any(),
                any()
            )
        } returns Result.success(
            RequestTokenResponse(true, EXPIRES_AT, REQUEST_TOKEN)
        )
        coEvery { authRemoteDataSource.createSession(REQUEST_TOKEN) } returns Result.success(
            SessionResponse(true, SESSION_ID)
        )
        coEvery { accountRemoteDataSource.getAccountDetails(SESSION_ID) } returns Result.success(
            AccountInfoResponse(
                id = ACCOUNT_ID,
                userName = USERNAME,
                name = USERNAME,
                avatar = null
            )
        )

        val result = repository.login(USERNAME, PASSWORD)

        assertTrue(result)
        coVerify {
            authenticationPreferences.saveSessionId(SESSION_ID)
            authenticationPreferences.saveUsername(USERNAME)
            authenticationPreferences.saveRequestToken(REQUEST_TOKEN)
            authenticationPreferences.setGuestMode(false)
            authenticationPreferences.saveAccountId(ACCOUNT_ID)
        }
        coVerify { accountRemoteDataSource.getAccountDetails(SESSION_ID) }
    }

    @Test
    fun `login returns false on failed login`() = runTest {
        coEvery { authRemoteDataSource.createRequestToken() } returns Result.success(
            RequestTokenResponse(true, EXPIRES_AT, REQUEST_TOKEN)
        )
        coEvery {
            authRemoteDataSource.validateLoginCredentials(
                any(),
                any(),
                any()
            )
        } returns Result.success(
            RequestTokenResponse(false, EXPIRES_AT, REQUEST_TOKEN)
        )

        val result = repository.login(USERNAME, WRONG_PASSWORD)

        assertFalse(result)
        coVerify(exactly = 0) { authenticationPreferences.saveSessionId(any()) }
        coVerify(exactly = 0) { accountRemoteDataSource.getAccountDetails(any()) }
    }

    @Test
    fun `login throws exception on network error during token creation`() = runTest {
        val networkError = RuntimeException("network error")
        coEvery { authRemoteDataSource.createRequestToken() } returns Result.failure(networkError)
        assertThrows(RuntimeException::class.java) {
            runTest { repository.login(USERNAME, PASSWORD) }
        }
    }

    @Test
    fun `login throws exception when createSession fails`() = runTest {
        val error = RuntimeException("session creation failed")

        coEvery { authRemoteDataSource.createRequestToken() } returns Result.success(
            RequestTokenResponse(true, EXPIRES_AT, REQUEST_TOKEN)
        )
        coEvery {
            authRemoteDataSource.validateLoginCredentials(
                any(),
                any(),
                any()
            )
        } returns Result.success(
            RequestTokenResponse(true, EXPIRES_AT, REQUEST_TOKEN)
        )
        coEvery { authRemoteDataSource.createSession(REQUEST_TOKEN) } returns Result.failure(error)

        assertThrows(RuntimeException::class.java) {
            runTest { repository.login(USERNAME, PASSWORD) }
        }
    }

    @Test
    fun `login throws exception when account details fetch fails`() = runTest {
        val accountError = RuntimeException("account details failed")

        coEvery { authRemoteDataSource.createRequestToken() } returns Result.success(
            RequestTokenResponse(true, EXPIRES_AT, REQUEST_TOKEN)
        )
        coEvery {
            authRemoteDataSource.validateLoginCredentials(
                any(),
                any(),
                any()
            )
        } returns Result.success(
            RequestTokenResponse(true, EXPIRES_AT, REQUEST_TOKEN)
        )
        coEvery { authRemoteDataSource.createSession(REQUEST_TOKEN) } returns Result.success(
            SessionResponse(true, SESSION_ID)
        )
        coEvery { accountRemoteDataSource.getAccountDetails(SESSION_ID) } returns Result.failure(
            accountError
        )

        assertThrows(RuntimeException::class.java) {
            runTest { repository.login(USERNAME, PASSWORD) }
        }
    }
    // endregion

    // region: loginAsGuest()
    @Test
    fun `loginAsGuest returns true on success and saves guest session ID`() = runTest {
        coEvery { authRemoteDataSource.createGuestSession() } returns Result.success(
            GuestSessionResponse(true, GUEST_SESSION_ID, "")
        )

        val result = repository.loginAsGuest()

        assertTrue(result)
        coVerify {
            authenticationPreferences.saveGuestSessionId(GUEST_SESSION_ID)
            authenticationPreferences.setGuestMode(true)
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
        every { authenticationPreferences.getSessionId() } returns SESSION_ID
        every { authenticationPreferences.isGuestMode() } returns false
        coEvery { authRemoteDataSource.deleteSession(SESSION_ID) } returns Result.success(
            DeleteSessionResponse(true)
        )

        val result = repository.logout()

        assertTrue(result)
        coVerify { authRemoteDataSource.deleteSession(SESSION_ID) }
        verify { authenticationPreferences.clearAuthentication() }
    }

    @Test
    fun `logout still returns true when no session exists`() = runTest {
        every { authenticationPreferences.getSessionId() } returns null

        val result = repository.logout()

        assertTrue(result)
        verify { authenticationPreferences.clearAuthentication() }
    }

    @Test
    fun `logout throws exception on delete session failure but still clears preferences`() =
        runTest {
            every { authenticationPreferences.getSessionId() } returns SESSION_ID
            every { authenticationPreferences.isGuestMode() } returns false
            val error = RuntimeException("delete failed")
            coEvery { authRemoteDataSource.deleteSession(SESSION_ID) } returns Result.failure(error)

            assertThrows(RuntimeException::class.java) {
                runTest { repository.logout() }
            }
        }

    @Test
    fun `logout skips delete session when in guest mode`() = runTest {
        every { authenticationPreferences.getSessionId() } returns SESSION_ID
        every { authenticationPreferences.isGuestMode() } returns true

        val result = repository.logout()

        assertTrue(result)
        coVerify(exactly = 0) { authRemoteDataSource.deleteSession(SESSION_ID) }
        verify { authenticationPreferences.clearAuthentication() }
    }
    // endregion

    // region: isLoggedIn()
    @Test
    fun `isLoggedIn returns true when user is logged in`() = runTest {
        every { authenticationPreferences.isLoggedIn() } returns true

        val result = repository.isLoggedIn()

        assertTrue(result)
    }

    @Test
    fun `isLoggedIn returns false when user is not logged in`() = runTest {
        every { authenticationPreferences.isLoggedIn() } returns false

        val result = repository.isLoggedIn()

        assertFalse(result)
    }
    // endregion

    @Test
    fun `loginAsGuest sets guest mode to true when successful`() = runTest {
        coEvery { authRemoteDataSource.createGuestSession() } returns Result.success(
            GuestSessionResponse(true, GUEST_SESSION_ID, "")
        )

        val result = repository.loginAsGuest()

        assertTrue(result)
        verify {
            authenticationPreferences.setGuestMode(true)
        }
    }

    @Test
    fun `logout deletes session when session exists and not in guest mode`() = runTest {
        every { authenticationPreferences.getSessionId() } returns SESSION_ID
        every { authenticationPreferences.isGuestMode() } returns false
        coEvery { authRemoteDataSource.deleteSession(SESSION_ID) } returns Result.success(
            DeleteSessionResponse(true)
        )

        val result = repository.logout()

        assertTrue(result)
        coVerify { authRemoteDataSource.deleteSession(SESSION_ID) }
    }

    companion object {
        private const val SESSION_ID = "sessionId123"
        private const val REQUEST_TOKEN = "requestT124"
        private const val USERNAME = "user"
        private const val PASSWORD = "pass"
        private const val WRONG_PASSWORD = "wrong"
        private const val GUEST_SESSION_ID = "guest123"
        private const val EXPIRES_AT = "123"
        private const val ACCOUNT_ID = 12345
    }
}