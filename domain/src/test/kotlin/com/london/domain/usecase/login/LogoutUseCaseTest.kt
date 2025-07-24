package com.london.domain.usecase.login

import com.google.common.truth.Truth.assertThat
import com.london.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LogoutUseCaseTest {

 private lateinit var authRepository: AuthRepository
 private lateinit var logoutUseCase: LogoutUseCase

 @Before
 fun setUp() {
  authRepository = mockk()
  logoutUseCase = LogoutUseCase(authRepository)
 }

 @Test
 fun `should return true when logout succeeds`() = runTest {
  // Given
  coEvery { authRepository.logout() } returns true

  // When
  val result = logoutUseCase.invoke()

  // Then
  coVerify(exactly = 1) { authRepository.logout() }
  assertThat(result).isTrue()
 }

 @Test
 fun `should return false when logout fails`() = runTest {
  // Given
  coEvery { authRepository.logout() } returns false

  // When
  val result = logoutUseCase.invoke()

  // Then
  coVerify(exactly = 1) { authRepository.logout() }
  assertThat(result).isFalse()
 }
}
