package com.london.novix.presentation.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.london.novix.presentation.designSystem.theme.NovixTheme
import com.london.novix.presentation.designSystem.theme.ThemePreviews
import com.london.novix.presentation.designSystem.theme.noRippleClickable

@Composable
fun NovixChip(
    text: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) NovixTheme.colors.secondary else NovixTheme.colors.surface,
        animationSpec = tween(durationMillis = 300),
        label = "background_color"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) NovixTheme.colors.onPrimary else NovixTheme.colors.body,
        animationSpec = tween(durationMillis = 300),
        label = "text_color"
    )

    val cornerRadius by animateDpAsState(
        targetValue = if (isSelected) 12.dp else 8.dp,
        animationSpec = tween(durationMillis = 300),
        label = "corner_radius"
    )

    val horizontalPadding by animateDpAsState(
        targetValue = if (isSelected) 24.dp else 12.dp,
        animationSpec = tween(durationMillis = 300),
        label = "horizontal_padding"
    )

    Text(
        text = text,
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .noRippleClickable(onClick)
            .padding(horizontal = horizontalPadding, vertical = 8.dp),
        color = textColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center,
        style = NovixTheme.typography.label.medium
    )
}

@Composable
private fun ScrollableChipGroup(
    chips: List<String>,
    modifier: Modifier = Modifier,
    selectedChip: String? = null,
    onChipSelected: (String) -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(chips) { chip ->
            NovixChip(
                text = chip,
                isSelected = chip == selectedChip,
                onClick = { onChipSelected(chip) }
            )
        }
    }
}

@ThemePreviews
@Composable
private fun ChipGroupPreview() {
    NovixTheme {
        val categories = listOf("All", "Adventure", "Action", "Comedy", "Drama", "Horror")
        Column(
            modifier = Modifier
                .background(NovixTheme.colors.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ScrollableChipGroup(
                chips = categories,
                selectedChip = "All",
                onChipSelected = {}
            )
        }
    }
}