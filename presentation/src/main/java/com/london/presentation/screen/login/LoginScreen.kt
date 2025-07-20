package com.london.presentation.screen.login

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.OutlinedTextField
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun LoginScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface),
    ) {
        Image(
            painter = painterResource(R.drawable.polygon1),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = (-100).dp, x = (-20).dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TopBar(title = "Login", onBackClick = {})
            Icon(
                painter = painterResource(id = R.drawable.novix_icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(64.dp)
                    .padding(top = 4.dp, bottom = 8.dp)
            )
            Text(
                stringResource(R.string.login_to_your_account),
                style = NovixTheme.typography.title.medium,
                color = NovixTheme.colors.title
            )
            Text(
                stringResource(R.string.user_name),
                style = NovixTheme.typography.title.small,
                color = NovixTheme.colors.title,
                modifier = Modifier.padding(top = 32.dp)
            )
            OutlinedTextField(
                value = TextFieldValue(),
                interactionSource = MutableInteractionSource(),
                onValueChange = {},
                leadingIcon = painterResource(com.london.designsystem.R.drawable.icon_user),
            )
            Text(
                "Password",
                style = NovixTheme.typography.title.small,
                color = NovixTheme.colors.title,
                modifier = Modifier.padding(top = 8.dp)
            )
            OutlinedTextField(
                value = TextFieldValue(),
                interactionSource = MutableInteractionSource(),
                onValueChange = {},
                leadingIcon = painterResource(R.drawable.lock_key),
                isPasswordField = true,
                passwordVisibleIcon = painterResource(id = com.london.designsystem.R.drawable.icon_show_password),
                passwordHiddenIcon = painterResource(id = com.london.designsystem.R.drawable.icon_hide_password),
                modifier = Modifier.padding(bottom = 26.dp)
            )
            PrimaryButton(
                text = "Login",
                hasLabel = true,
                hasIcon = false,
                isLoading = false,
                onClick = {},
                icon = null,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "Forgot Password?",
                style = NovixTheme.typography.label.medium,
                color = NovixTheme.colors.primary,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Don't have an account?",
                    style = NovixTheme.typography.body.small,
                    color = NovixTheme.colors.body,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    "Create account",
                    style = NovixTheme.typography.label.medium,
                    color = NovixTheme.colors.primary,
                )
            }
        }
    }


}

@Preview
@Composable
private fun LoginScreenPreview() {
    NovixTheme {
        LoginScreen()
    }
}