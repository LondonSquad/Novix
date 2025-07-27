package com.london.presentation.screen.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        WelcomePoster(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.5f)
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

@Composable
fun WelcomePoster(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.image_onboarding_background),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.00f to Color.Transparent,
                            0.20f to NovixTheme.colors.surface.copy(alpha = 0.10f),
                            0.45f to NovixTheme.colors.surface.copy(alpha = 0.35f),
                            0.75f to NovixTheme.colors.surface.copy(alpha = 0.70f),
                            0.90f to NovixTheme.colors.surface.copy(alpha = 0.85f),
                            1.00f to NovixTheme.colors.surface.copy(alpha = 1f),
                        )
                    )
                )
        )
        Image(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 32.dp)
        )
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
