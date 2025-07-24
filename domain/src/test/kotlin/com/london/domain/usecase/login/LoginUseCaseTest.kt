package com.london.domain.usecase.login

import com.google.common.truth.Truth.assertThat
import com.london.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {

 private lateinit var authRepository: AuthRepository
 private lateinit var loginUseCase: LoginUseCase

 @Before
 fun setUp() {
  authRepository = mockk()
  loginUseCase = LoginUseCase(authRepository)
 }

 @Test
 fun `should return true when login succeeds`() = runTest {
  // Given
  val username = "Bassant"
  val password = "password123"
  coEvery { authRepository.login(username, password) } returns true

  // When
  val result = loginUseCase(username, password)

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
  val result = loginUseCase(username, password)

  // Then
  coVerify(exactly = 1) { authRepository.login(username, password) }
  assertThat(result).isFalse()
 }
}
