package com.london.presentation.feature.buildscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.component.Text
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.OutlineButton
import com.london.designsystem.theme.NovixTheme

@Composable
fun NetworkErrorScreen(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
    onBack: (() -> Unit)?
) {
    Box(
        modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        if (onBack != null) {
            TopBar(
                onBackClick = onBack,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.image_no_internet),
                contentDescription = stringResource(R.string.no_internet),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(82.dp, 64.dp)
            )

            Text(
                text = stringResource(R.string.you_are_offline),
                style = NovixTheme.typography.title.medium,
                color = NovixTheme.colors.title,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.check_your_connection),
                style = NovixTheme.typography.body.small,
                color = NovixTheme.colors.body,
                textAlign = TextAlign.Center
            )
            OutlineButton(
                text = stringResource(com.london.presentation.R.string.retry),
                hasLabel = true,
                icon = null,
                hasIcon = false,
                isLoading = false,
                onClick = onRetry,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }

}
