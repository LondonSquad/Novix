package com.london.presentation.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.component.Icon
import com.london.designsystem.component.OutlinedTextField
import com.london.designsystem.component.Text
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import org.koin.androidx.compose.koinViewModel
import com.london.designsystem.R as dsR

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.NavigateToHome -> onNavigateToHome()
                is LoginEffect.NavigateToCreateAccount -> uriHandler.openUri(effect.url)
                is LoginEffect.NavigateToForgotPassword -> uriHandler.openUri(effect.url)
                is LoginEffect.NavigateBack -> onNavigateBack()
                null -> {}
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Content(
            uiState = uiState,
            loginContract = viewModel
        )
    }
}

@Composable
private fun Content(
    uiState: LoginUiState,
    loginContract: LoginContract
) {
    val interactionSourceUserName = remember { MutableInteractionSource() }
    val interactionSourcePassword = remember { MutableInteractionSource() }
    val isLoadingGeneral = uiState.isLoading || uiState.isGuestLoginLoading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface),
    ) {
        Image(
            painter = painterResource(dsR.drawable.polygon1),
            contentDescription = stringResource(R.string.app_icon),
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = (-60).dp, x = (-20).dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.statusBars.asPaddingValues())
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            TopBar(
                title = stringResource(R.string.login),
                modifier = Modifier.padding(top = 12.dp),
                onBackClick = loginContract::onNavigateBack
            )

            Icon(
                painter = painterResource(id = dsR.drawable.novix_icon),
                contentDescription = stringResource(R.string.app_icon),
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .size(64.dp)
            )

            Text(
                stringResource(R.string.login_to_your_account),
                style = NovixTheme.typography.title.medium,
                color = NovixTheme.colors.title,
                modifier = Modifier.padding(top = 16.dp, bottom = 40.dp)
            )

            OutlinedTextField(
                value = uiState.username,
                label = stringResource(R.string.username),
                interactionSource = interactionSourceUserName,
                onValueChange = loginContract::onUsernameChanged,
                leadingIcon = painterResource(dsR.drawable.icon_user),
            )

            OutlinedTextField(
                value = uiState.password,
                onValueChange = loginContract::onPasswordChanged,
                label = stringResource(R.string.password),
                isPasswordField = true,
                passwordVisible = uiState.passwordVisible,
                onPasswordVisibilityChange = loginContract::onPasswordVisibilityToggled,
                passwordVisibleIcon = painterResource(id = dsR.drawable.icon_show_password),
                passwordHiddenIcon = painterResource(id = dsR.drawable.icon_hide_password),
                interactionSource = interactionSourcePassword,
                leadingIcon = painterResource(dsR.drawable.lock_key),
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                enabled = !isLoadingGeneral
            )

            PrimaryButton(
                text = stringResource(R.string.login),
                hasLabel = true,
                hasIcon = false,
                isLoading = uiState.isLoading,
                onClick = loginContract::onLoginClick,
                icon = null,
                enabled = uiState.isLoginEnabled && !isLoadingGeneral,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                stringResource(R.string.forgot_password),
                style = NovixTheme.typography.label.medium,
                color = NovixTheme.colors.primary,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = !isLoadingGeneral
                    ) {
                        loginContract.onForgotPasswordClick()
                    }
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.navigationBars.asPaddingValues()),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.don_t_have_an_account),
                    style = NovixTheme.typography.body.small,
                    color = NovixTheme.colors.body,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    stringResource(R.string.create_account),
                    style = NovixTheme.typography.label.medium,
                    color = NovixTheme.colors.primary,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = !isLoadingGeneral
                    ) {
                        loginContract.onCreateAccountClick()
                    }
                )
            }
        }
    }
}