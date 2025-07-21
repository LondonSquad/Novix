package com.london.presentation.screen.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
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


            Image(
                painter = painterResource(id = R.drawable.bg_logo_with_fog),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.4f)
                .background(NovixTheme.colors.surface)
                .padding(horizontal = 16.dp, vertical = 32.dp),
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
                enabled = true,
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

@ThemePreviews
@Composable
fun PreviewWelcomeScreen() {
    WelcomeScreen(
        onContinueClicked = {},
        onLoginClicked = {}
    )
}