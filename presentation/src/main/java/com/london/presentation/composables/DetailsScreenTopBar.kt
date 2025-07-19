package com.london.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.component.ButtonIcon
import com.london.designsystem.component.SaveIcon
import com.london.designsystem.theme.NovixTheme

@Composable
fun DetailsScreenTopBar(
    modifier: Modifier = Modifier,
    isSaved: Boolean,
    backgroundAlpha: Float,
    onBackClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                NovixTheme.colors.surface.copy(alpha = backgroundAlpha)
            )
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            ),
        contentAlignment = Alignment.Center
    ) {
        ButtonIcon(
            onClick = onBackClick,
            iconRes = R.drawable.arrow_left,
            backgroundColor = NovixTheme.colors.iconBackgroundLow,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp, bottom = 8.dp)
                .size(40.dp)
                .align(Alignment.TopStart),
        )

        SaveIcon(
            isSaved = isSaved,
            onSaveClick = {
                // TODO on save the show
            },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp, bottom = 8.dp)
                .size(40.dp)
                .clip(RoundedCornerShape(16))
                .align(Alignment.TopEnd),
            backgroundColor = NovixTheme.colors.iconBackgroundLow
        )
    }
}