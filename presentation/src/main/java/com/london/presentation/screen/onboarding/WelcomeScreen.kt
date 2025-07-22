package com.london.presentation.screen.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.london.designsystem.component.Icon
import com.london.designsystem.component.Text
import com.london.designsystem.component.button.OutlineButton
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.R

@Composable
fun WelcomeScreen(
    onLoginClicked: () -> Unit,
    onContinueClicked: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.image_onboarding_background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Icon(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = null,
                tint = NovixTheme.colors.primary,
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 6.dp)
                    .zIndex(1f)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.3f)
                .background(NovixTheme.colors.surface)
                .padding(start = 16.dp, end = 16.dp, bottom = 50.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.welcome_title),
                    style = NovixTheme.typography.title.large,
                    color = NovixTheme.colors.title
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.welcome_description),
                    style = NovixTheme.typography.body.small,
                    color = NovixTheme.colors.body,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                PrimaryButton(
                    text = stringResource(R.string.login),
                    onClick = onLoginClicked,
                    modifier = Modifier
                        .fillMaxWidth(),
                    isLoading = false,
                    hasIcon = false,
                    hasLabel = true,
                    icon = null
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlineButton(
                    text = stringResource(R.string.continue_as_guest),
                    onClick = onContinueClicked,
                    modifier = Modifier
                        .fillMaxWidth(),
                    hasLabel = true,
                    icon = null,
                    hasIcon = false,
                    isLoading = false,
                )
            }
        }
    }
}

@ThemePreviews
@Composable
fun PreviewWelcomeScreen() {
    WelcomeScreen(
        onContinueClicked = {},
        onLoginClicked = {}
    )
}
