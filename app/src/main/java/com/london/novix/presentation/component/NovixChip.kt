package com.london.novix.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.london.novix.presentation.designSystem.theme.NovixTheme
import com.london.novix.presentation.designSystem.theme.ThemePreviews
import com.london.novix.presentation.designSystem.theme.noRippleClickable

enum class ChipVariant {
    Default,
    Variant2
}

@Composable
fun NovixChip(
    text: String,
    modifier: Modifier = Modifier,
    variant: ChipVariant = ChipVariant.Default,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val colors = NovixTheme.colors
    val typography = NovixTheme.typography

    val backgroundColor = when (variant) {
        ChipVariant.Default -> if (isSelected) colors.secondary else colors.surface
        ChipVariant.Variant2 -> colors.surface
    }

    val textColor = when (variant) {
        ChipVariant.Default -> if (isSelected) colors.onPrimary else colors.body
        ChipVariant.Variant2 -> colors.body
    }

    val shape = when (variant) {
        ChipVariant.Default -> RoundedCornerShape(12.dp)
        ChipVariant.Variant2 -> RoundedCornerShape(8.dp)
    }

    val verticalPadding = when (variant) {
        ChipVariant.Default -> 8.dp
        ChipVariant.Variant2 -> 8.dp
    }

    val horizontalPadding = when (variant) {
        ChipVariant.Default -> 24.dp
        ChipVariant.Variant2 -> 12.dp
    }

    Row(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .noRippleClickable(onClick)
            .padding(
                horizontal = horizontalPadding,
                vertical = verticalPadding
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            style = typography.label.medium
        )
    }
}

@Composable
fun ScrollableChipGroup(
    chips: List<String>,
    modifier: Modifier = Modifier,
    selectedChip: String? = null,
    onChipSelected: (String) -> Unit,
    variant: ChipVariant = ChipVariant.Default
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(chips.size) { index ->
            NovixChip(
                text = chips[index],
                variant = variant,
                isSelected = chips[index] == selectedChip,
                onClick = { onChipSelected(chips[index]) }
            )
        }
    }
}

@ThemePreviews
@Composable
private fun ChipGroupPreview() {
    NovixTheme {
        val categories = listOf("All", "Adventure", "Action", "Comedy", "Drama", "Sci-Fi", "Horror")

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