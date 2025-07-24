package com.london.domain.usecase.login

import com.google.common.truth.Truth.assertThat
import com.london.domain.repository.AuthRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class StartWebAuthUseCaseTest {

 private lateinit var authRepository: AuthRepository
 private lateinit var startWebAuthUseCase: StartWebAuthUseCase

 @Before
 fun setUp() {
  authRepository = mockk()
  startWebAuthUseCase = StartWebAuthUseCase(authRepository)
 }

 @Test
 fun `should emit true when session is valid`() = runTest {
  // Given
  every { authRepository.validateSession() } returns flowOf(true)

  // When
  val result = startWebAuthUseCase().toList()

  // Then
  verify(exactly = 1) { authRepository.validateSession() }
  assertThat(result).containsExactly(true)
 }

 @Test
 fun `should emit false when session is invalid`() = runTest {
  // Given
  every { authRepository.validateSession() } returns flowOf(false)

  // When
  val result = startWebAuthUseCase().toList()

  // Then
  verify(exactly = 1) { authRepository.validateSession() }
  assertThat(result).containsExactly(false)
 }
}
