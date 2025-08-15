package com.london.presentation.feature.authentication.login

import android.app.Application
import androidx.compose.ui.text.input.TextFieldValue
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.usecase.authentication.AuthenticationUseCase
import com.london.presentation.R
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var authenticationUseCase: AuthenticationUseCase
    private lateinit var context: Application
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authenticationUseCase = mockk()
        context = mockk()
        every { context.getString(R.string.login_failed) } returns "Login failed"
        every { context.getString(R.string.guest_login_failed) } returns "Guest login failed"
        viewModel = LoginViewModel(context, authenticationUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onUsernameChanged updates state correctly`() = runTest {
        val testUsername = TextFieldValue("testUser")

        viewModel.onUsernameChanged(testUsername)

        assertThat(viewModel.state.value.username).isEqualTo(testUsername)
        assertThat(viewModel.state.value.isLoginEnabled).isFalse()
        assertThat(viewModel.state.value.error).isNull()
    }

    @Test
    fun `onPasswordChanged updates state correctly`() = runTest {
        val testPassword = TextFieldValue("password123")

        viewModel.onPasswordChanged(testPassword)

        assertThat(viewModel.state.value.password).isEqualTo(testPassword)
        assertThat(viewModel.state.value.isLoginEnabled).isFalse()
        assertThat(viewModel.state.value.error).isNull()
    }

    @Test
    fun `onPasswordVisibilityToggled toggles visibility`() = runTest {
        val initialVisibility = viewModel.state.value.passwordVisible

        viewModel.onPasswordVisibilityToggled()

        assertThat(viewModel.state.value.passwordVisible).isNotEqualTo(initialVisibility)
    }

    @Test
    fun `onCreateAccountClick emits NavigateToRegistration effect`() = runTest {
        viewModel.effect.test {
            viewModel.onCreateAccountClick()
            assertThat(awaitItem()).isEqualTo(LoginEffect.NavigateToRegistration)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onForgotPasswordClick emits NavigateToForgotPassword effect on first call`() = runTest {
        viewModel.effect.test {
            viewModel.onForgotPasswordClick()

            val emittedEffect = awaitItem()
            assertThat(emittedEffect).isInstanceOf(LoginEffect.NavigateToForgotPassword::class.java)

            val url = (emittedEffect as LoginEffect.NavigateToForgotPassword).url
            assertThat(url).startsWith("$FORGOT_PASSWORD_URL?t=")

            val timestampPart = url.substringAfter("?t=")
            assertThat(timestampPart.toLongOrNull()).isNotNull()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onNavigateBack emits NavigateBack effect`() = runTest {
        viewModel.effect.test {
            viewModel.onNavigateBack()
            assertThat(awaitItem()).isEqualTo(LoginEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onLoginClick with empty credentials does nothing`() = runTest {
        viewModel.effect.test {
            viewModel.onLoginClick()
            expectNoEvents()
        }
    }

    @Test
    fun `onLoginClick with valid credentials performs login`() = runTest {
        // Arrange
        coEvery { authenticationUseCase.login(any(), any()) } returns true
        viewModel.onUsernameChanged(TextFieldValue("user"))
        viewModel.onPasswordChanged(TextFieldValue("password123"))

        // Act & Assert
        viewModel.effect.test {
            viewModel.onLoginClick()
            assertThat(awaitItem()).isEqualTo(LoginEffect.NavigateToHome)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { authenticationUseCase.login("user", "password123") }
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    private companion object {
        const val FORGOT_PASSWORD_URL = "https://www.themoviedb.org/reset-password"
    }
}