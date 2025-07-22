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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.component.Icon
import com.london.designsystem.component.OutlinedTextField
import com.london.designsystem.component.Text
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.R.string

@Composable
fun LoginScreen() {
    val username = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    val isLoginEnabled = username.value.text.isNotEmpty() && password.value.text.isNotEmpty()

    Content(
        username = username.value,
        onUsernameChange = {},
        password = password.value,
        onPasswordChange = {},
        onLoginClick = {},
        onForgotPasswordClick = {},
        isLoginEnabled = isLoginEnabled,
        onCreateAccountClick = {},
    )
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
private fun Content(
    username: TextFieldValue,
    onUsernameChange: (TextFieldValue) -> Unit,
    password: TextFieldValue,
    onPasswordChange: (TextFieldValue) -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    isLoginEnabled: Boolean,
    onCreateAccountClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface),
    ) {
        Image(
            painter = painterResource(R.drawable.polygon1),
            contentDescription = stringResource(string.app_icon),
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
                title = stringResource(string.login),
                modifier = Modifier.padding(top = 8.dp), onBackClick = {}
            )
            Icon(
                painter = painterResource(id = R.drawable.novix_icon),
                contentDescription = stringResource(string.app_icon),
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(64.dp)
                    .padding(top = 12.dp, bottom = 16.dp)
            )
            Text(
                stringResource(string.login_to_your_account),
                style = NovixTheme.typography.title.medium,
                color = NovixTheme.colors.title
            )
            Text(
                stringResource(string.user_name),
                style = NovixTheme.typography.title.small,
                color = NovixTheme.colors.title,
                modifier = Modifier.padding(top = 40.dp, bottom = 8.dp)
            )
            OutlinedTextField(
                value = username,
                interactionSource = MutableInteractionSource(),
                onValueChange = onUsernameChange,
                leadingIcon = painterResource(R.drawable.icon_user),
            )
            Text(
                stringResource(string.password),
                style = NovixTheme.typography.title.small,
                color = NovixTheme.colors.title,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            OutlinedTextField(
                value = password,
                interactionSource = MutableInteractionSource(),
                onValueChange = onPasswordChange,
                leadingIcon = painterResource(R.drawable.lock_key),
                isPasswordField = true,
                passwordVisibleIcon = painterResource(id = R.drawable.icon_show_password),
                passwordHiddenIcon = painterResource(id = R.drawable.icon_hide_password),
                modifier = Modifier.padding(bottom = 32.dp)
            )
            PrimaryButton(
                text = stringResource(string.login),
                hasLabel = true,
                hasIcon = false,
                isLoading = false,
                onClick = onLoginClick,
                icon = null,
                enabled = isLoginEnabled,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                stringResource(string.forgot_password),
                style = NovixTheme.typography.label.medium,
                color = NovixTheme.colors.primary,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(string.don_t_have_an_account),
                    style = NovixTheme.typography.body.small,
                    color = NovixTheme.colors.body,
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .clickable { onForgotPasswordClick() })
                Text(
                    stringResource(string.create_account),
                    style = NovixTheme.typography.label.medium,
                    color = NovixTheme.colors.primary,
                    modifier = Modifier.clickable {
                        onCreateAccountClick()
                    }
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun Preview() {
    NovixTheme {
        LoginScreen()
    }
}