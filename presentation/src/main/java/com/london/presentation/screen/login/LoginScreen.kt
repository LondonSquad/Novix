package com.london.presentation.screen.login

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.Icon
import com.london.designsystem.component.OutlinedTextField
import com.london.designsystem.component.Text
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import org.koin.androidx.compose.koinViewModel


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    val isLoginEnabled = state.username.isNotEmpty() && state.password.isNotEmpty()
    if (state.launchUrl != null) {
        WebViewScreen(
            url = state.launchUrl!!,
            onBackPressed = {
                viewModel.clearUrl()
            }
        )
    } else {
        Content(
            username = TextFieldValue(state.username),
            onUsernameChange = { viewModel.onUsernameChange(it.text) },
            password = TextFieldValue(state.password),
            onPasswordChange = { viewModel.onPasswordChange(it.text) },
            onCreateAccountClicked = { viewModel.onCreateAccountClicked() },
            onForgotPasswordClick = { viewModel.onForgotPasswordClicked() },
            isLoginEnabled = isLoginEnabled,
            onLoggedIn = { viewModel.login() },
            loginAsGuest = { viewModel.loginAsGuest() }
        )
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
private fun Content(
    username: TextFieldValue,
    onUsernameChange: (TextFieldValue) -> Unit,
    password: TextFieldValue,
    onPasswordChange: (TextFieldValue) -> Unit,
    onCreateAccountClicked: () -> Unit,
    onLoggedIn: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    isLoginEnabled: Boolean,
    loginAsGuest: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
            .padding(bottom = 16.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.star),
            contentDescription = stringResource(R.string.search),
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = (-60).dp, x = (-20).dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.statusBars.asPaddingValues())
                .padding(horizontal = 16.dp)
        ) {
            TopBar(
                title = stringResource(R.string.search),
                modifier = Modifier.padding(top = 8.dp),
                onBackClick = {}
            )

            Icon(
                painter = painterResource(id = R.drawable.star),
                contentDescription = stringResource(R.string.star),
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(64.dp)
                    .padding(top = 12.dp, bottom = 16.dp)
            )

            Text(
                stringResource(R.string.star),
                style = NovixTheme.typography.title.medium,
                color = NovixTheme.colors.title
            )

            Text(
                stringResource(R.string.search),
                style = NovixTheme.typography.title.small,
                color = NovixTheme.colors.title,
                modifier = Modifier.padding(top = 40.dp, bottom = 8.dp)
            )

            OutlinedTextField(
                value = username,
                interactionSource = MutableInteractionSource(),
                onValueChange = onUsernameChange,
                leadingIcon = painterResource(com.london.designsystem.R.drawable.icon_user),
            )

            Text(
                stringResource(R.string.star),
                style = NovixTheme.typography.title.small,
                color = NovixTheme.colors.title,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            OutlinedTextField(
                value = password,
                interactionSource = MutableInteractionSource(),
                onValueChange = onPasswordChange,
                leadingIcon = painterResource(R.drawable.star),

                isPasswordField = true,
                passwordVisibleIcon = painterResource(id = com.london.designsystem.R.drawable.icon_show_password),
                passwordHiddenIcon = painterResource(id = com.london.designsystem.R.drawable.icon_hide_password),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            PrimaryButton(
                text = stringResource(R.string.search),
                hasLabel = true,
                hasIcon = false,
                isLoading = false,
                onClick = onLoggedIn,
                icon = null,
                enabled = isLoginEnabled,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                stringResource(R.string.star),
                style = NovixTheme.typography.label.medium,
                color = NovixTheme.colors.primary,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable { onCreateAccountClicked() }
            )

            Text(
                "Login as a Guest ",
                style = NovixTheme.typography.label.medium,
                color = NovixTheme.colors.primary,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable { loginAsGuest() }
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.star),
                    style = NovixTheme.typography.body.small,
                    color = NovixTheme.colors.body,
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .clickable { onForgotPasswordClick() }
                )

                Text(
                    stringResource(R.string.imagr_dot),
                    style = NovixTheme.typography.label.medium,
                    color = NovixTheme.colors.primary,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    NovixTheme {
        LoginScreen()
    }
}