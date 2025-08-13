package com.london.domain.usecase.login

import com.google.common.truth.Truth.assertThat
import com.london.domain.repository.AccountRepository
import com.london.domain.repository.AuthenticationRepository
import com.london.domain.usecase.authentication.AuthenticationUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthenticationUseCaseTest {

    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var authRepository: AuthenticationRepository
    private lateinit var accountRepository: AccountRepository
    private lateinit var authenticationUseCase: AuthenticationUseCase

    // region LoginAsGuest
    @Before
    fun setUp() {
        authenticationRepository = mockk()
        authenticationUseCase = AuthenticationUseCase(authenticationRepository)
        authRepository = mockk()
        accountRepository = mockk()
        authenticationUseCase = AuthenticationUseCase(
            repository = authenticationRepository,
        )
    }

    @Test
    fun `should return true when login as guest succeeds`() = runTest {
        // Given
        coEvery { authenticationRepository.loginAsGuest() } returns true

        // When
        val result = authenticationUseCase.loginAsGuest()

        // Then
        coVerify(exactly = 1) { authenticationRepository.loginAsGuest() }
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when login as guest fails`() = runTest {
        // Given
        coEvery { authenticationRepository.loginAsGuest() } returns false

        // When
        val result = authenticationUseCase.loginAsGuest()

        // Then
        coVerify(exactly = 1) { authenticationRepository.loginAsGuest() }
        assertThat(result).isFalse()
    }

    @Test
    fun `should call loginAsGuest and return result`() = runTest {
        // Arrange
        val expected = true
        coEvery { authenticationRepository.loginAsGuest() } returns expected

        // Act
        val result = authenticationUseCase.loginAsGuest()

        // Assert
        coVerify(exactly = 1) { authenticationRepository.loginAsGuest() }
        assertThat(result).isEqualTo(expected)
    }
    // endregion

    // region Login
    @Test
    fun `should return true when login succeeds`() = runTest {
        // Given
        val username = "Bassant"
        val password = "password123"
        coEvery { authenticationRepository.login(username, password) } returns true

        // When
        val result = authenticationUseCase.login(username, password)

        // Then
        coVerify(exactly = 1) { authenticationRepository.login(username, password) }
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when login fails`() = runTest {
        // Given
        val username = "Bassant"
        val password = "wrongPassword"
        coEvery { authenticationRepository.login(username, password) } returns false

        // When
        val result = authenticationUseCase.login(username, password)

        // Then
        coVerify(exactly = 1) { authenticationRepository.login(username, password) }
        assertThat(result).isFalse()
    }
    // endregion

    // region Logout
    @Test
    fun `should return true when logout succeeds`() = runTest {
        // Given
        coEvery { authenticationRepository.logout() } returns true

        // When
        val result = authenticationUseCase.logout()

        // Then
        coVerify(exactly = 1) { authenticationRepository.logout() }
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when logout fails`() = runTest {
        // Given
        coEvery { authenticationRepository.logout() } returns false

        // When
        val result = authenticationUseCase.logout()

        // Then
        coVerify(exactly = 1) { authenticationRepository.logout() }
        assertThat(result).isFalse()
    }
    // endregion

    @Test
    fun `should return false when User not logged in`() = runTest {
        // Given
        coEvery { authenticationRepository.isLoggedIn() } returns false

        // When
        val result = authenticationUseCase.isLoggedIn()

        // Then
        coVerify(exactly = 1) { authenticationRepository.isLoggedIn() }
        assertThat(result).isFalse()
    }

    @Test
    fun `should return true when User logged in`() = runTest {
        // Given
        coEvery { authenticationRepository.isLoggedIn() } returns true

        // When
        val result = authenticationUseCase.isLoggedIn()

        // Then
        coVerify(exactly = 1) { authenticationRepository.isLoggedIn() }
        assertThat(result).isTrue()
    }
}
