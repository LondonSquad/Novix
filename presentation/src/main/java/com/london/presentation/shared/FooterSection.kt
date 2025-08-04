package com.london.presentation.shared

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.button.PrimaryButton
import com.london.presentation.R
import com.london.presentation.R.drawable

@SuppressLint("SuspiciousIndentation")
@Composable
fun FooterSection(
    haveTrailer: Boolean,
    modifier: Modifier,
    isRateEnabled: Boolean = true,
    onStarClick: () -> Unit,
    onRateClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D0608).copy(alpha = 0.0f),
                        Color(0xFF0D0608).copy(alpha = 1.0f)
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                ),
            )
            .padding(horizontal = if (haveTrailer) 16.dp else 24.dp)
            .padding(
                bottom = 24.dp + WindowInsets.navigationBars.asPaddingValues()
                    .calculateBottomPadding()
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (isRateEnabled) {
            PrimaryButton(
                text = null,
                onClick = onRateClick,
                hasLabel = false,
                icon = drawable.movie_button_star,
                hasIcon = true,
                isLoading = false,
            )
        }

        PrimaryButton(
            text = stringResource(R.string.play_trailer),
            onClick = onStarClick,
            hasLabel = true,
            hasIcon = false,
            isLoading = false,
            enabled = haveTrailer,
            icon = null,
            modifier = Modifier.weight(1f)
        )
    }
}