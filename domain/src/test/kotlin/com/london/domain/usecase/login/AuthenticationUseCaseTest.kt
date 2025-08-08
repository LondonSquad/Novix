package com.london.domain.usecase.login

import com.google.common.truth.Truth.assertThat
import com.london.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthenticationUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var authenticationUseCase: AuthenticationUseCase

    // region LoginAsGuest
    @Before
    fun setUp() {
        authRepository = mockk()
        authenticationUseCase = AuthenticationUseCase(authRepository)
    }

    @Test
    fun `should return true when login as guest succeeds`() = runTest {
        // Given
        coEvery { authRepository.loginAsGuest() } returns true

        // When
        val result = authenticationUseCase.loginAsGuest()

        // Then
        coVerify(exactly = 1) { authRepository.loginAsGuest() }
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when login as guest fails`() = runTest {
        // Given
        coEvery { authRepository.loginAsGuest() } returns false

        // When
        val result = authenticationUseCase.loginAsGuest()

        // Then
        coVerify(exactly = 1) { authRepository.loginAsGuest() }
        assertThat(result).isFalse()
    }

    @Test
    fun `should call loginAsGuest and return result`() = runTest {
        // Arrange
        val expected = true
        coEvery { authRepository.loginAsGuest() } returns expected

        // Act
        val result = authenticationUseCase.loginAsGuest()

        // Assert
        coVerify(exactly = 1) { authRepository.loginAsGuest() }
        assertThat(result).isEqualTo(expected)
    }
    // endregion

    // region Login
    @Test
    fun `should return true when login succeeds`() = runTest {
        // Given
        val username = "Bassant"
        val password = "password123"
        coEvery { authRepository.login(username, password) } returns true

        // When
        val result = authenticationUseCase.login(username, password)

        // Then
        coVerify(exactly = 1) { authRepository.login(username, password) }
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when login fails`() = runTest {
        // Given
        val username = "Bassant"
        val password = "wrongPassword"
        coEvery { authRepository.login(username, password) } returns false

        // When
        val result = authenticationUseCase.login(username, password)

        // Then
        coVerify(exactly = 1) { authRepository.login(username, password) }
        assertThat(result).isFalse()
    }
    // endregion

    // region Logout
    @Test
    fun `should return true when logout succeeds`() = runTest {
        // Given
        coEvery { authRepository.logout() } returns true

        // When
        val result = authenticationUseCase.logout()

        // Then
        coVerify(exactly = 1) { authRepository.logout() }
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when logout fails`() = runTest {
        // Given
        coEvery { authRepository.logout() } returns false

        // When
        val result = authenticationUseCase.logout()

        // Then
        coVerify(exactly = 1) { authRepository.logout() }
        assertThat(result).isFalse()
    }
    // endregion

    @Test
    fun `should return false when User not logged in`() = runTest {
        // Given
        coEvery { authRepository.isLoggedIn() } returns false

        // When
        val result = authenticationUseCase.isLoggedIn()

        // Then
        coVerify(exactly = 1) { authRepository.isLoggedIn() }
        assertThat(result).isFalse()
    }

    @Test
    fun `should return true when User logged in`() = runTest {
        // Given
        coEvery { authRepository.isLoggedIn() } returns true

        // When
        val result = authenticationUseCase.isLoggedIn()

        // Then
        coVerify(exactly = 1) { authRepository.isLoggedIn() }
        assertThat(result).isTrue()
    }
}
