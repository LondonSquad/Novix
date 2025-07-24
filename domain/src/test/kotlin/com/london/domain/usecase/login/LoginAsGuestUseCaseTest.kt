package com.london.domain.usecase.login

import com.london.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class LoginAsGuestUseCaseTest {

 private lateinit var authRepository: AuthRepository
 private lateinit var loginAsGuestUseCase: LoginAsGuestUseCase

 @Before
 fun setUp() {
  authRepository = mockk()
  loginAsGuestUseCase = LoginAsGuestUseCase(authRepository)
 }

 @Test
 fun `should return true when login as guest succeeds`() = runTest {
  // Given
  coEvery { authRepository.loginAsGuest() } returns true

  // When
  val result = loginAsGuestUseCase()

  // Then
  coVerify(exactly = 1) { authRepository.loginAsGuest() }
  assertThat(result).isTrue()
 }

 @Test
 fun `should return false when login as guest fails`() = runTest {
  // Given
  coEvery { authRepository.loginAsGuest() } returns false

  // When
  val result = loginAsGuestUseCase()

  // Then
  coVerify(exactly = 1) { authRepository.loginAsGuest() }
  assertThat(result).isFalse()
 }
}
