package com.london.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews

@Composable
fun Selection(
    mainText: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    subText: String? = null,
    onClick: () -> Unit = {}
) {
    val backgroundColor by animateColorAsState(
        if (isSelected) NovixTheme.colors.primaryVariant
        else NovixTheme.colors.surface
    )
    val borderColor by animateColorAsState(
        if (isSelected) NovixTheme.colors.primary
        else NovixTheme.colors.stroke
    )
    val borderWidth = if (isSelected) (1.5).dp else 1.dp

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color = backgroundColor)
            .border(width = borderWidth, color = borderColor, shape = RoundedCornerShape(12.dp))
            .clickable(onClick = onClick, indication = null, interactionSource = null)
            .padding(horizontal = 12.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = mainText,
            style = NovixTheme.typography.label.large,
            color = NovixTheme.colors.body,
        )
        subText?.let {
            Text(
                text = subText,
                style = NovixTheme.typography.label.small,
                color = NovixTheme.colors.hint,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun SelectionSelectedPreview() {
    NovixTheme {
        Selection(
            mainText = "My favorite", subText = "12 items", isSelected = true
        )
    }
}

@ThemePreviews
@Composable
private fun SelectionNotSelectedPreview() {
    NovixTheme {
        Selection(
            mainText = "My favorite",
        )
    }
}