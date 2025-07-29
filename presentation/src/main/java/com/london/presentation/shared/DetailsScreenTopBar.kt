package com.london.presentation.shared

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
import com.london.designsystem.component.SaveIcon
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme

@Composable
fun DetailsScreenTopBar(
    modifier: Modifier = Modifier,
    isSaved: Boolean,
    backgroundAlpha: Float,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit = {
        // TODO on save the show
    }
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                NovixTheme.colors.surface.copy(alpha = backgroundAlpha)
            )
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 12.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        TopBar(
            onBackClick = onBackClick,
            modifier = Modifier.padding(horizontal = 16.dp),
            customEndContent = {
                SaveIcon(
                    isSaved = isSaved,
                    onSaveClick = onSaveClick,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(16)),
                    backgroundColor = NovixTheme.colors.iconBackgroundLow,
                    roundCorner = 12
                )
            }
        )
    }
}