package com.london.domain.usecase.login

import com.google.common.truth.Truth.assertThat
import com.london.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LoginAsGuestUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var useCase: LoginAsGuestUseCase

    @Before
    fun setup() {
        authRepository = mockk()
        useCase = LoginAsGuestUseCase(authRepository)
    }

    @Test
    fun `invoke() should call loginAsGuest and return result`() = runTest {
        // Arrange
        val expected = true
        coEvery { authRepository.loginAsGuest() } returns expected

        // Act
        val result = useCase.invoke()

        // Assert
        coVerify(exactly = 1) { authRepository.loginAsGuest() }
        assertThat(result).isEqualTo(expected)
    }
}
