package com.london.novix.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.novix.R
import com.london.novix.presentation.designSystem.theme.NovixTheme
import com.london.novix.presentation.designSystem.theme.ThemePreviews
import com.london.novix.presentation.designSystem.theme.noRippleClickable

@Composable
fun SectionHeader(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(color = NovixTheme.colors.surface)
    ) {
        Text(
            text = text,
            style = NovixTheme.typography.headline.small,
            color = NovixTheme.colors.title,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.noRippleClickable(onClick),
        ) {
            Text(
                text = stringResource(R.string.all),
                style = NovixTheme.typography.label.medium,
                color = NovixTheme.colors.primary,
            )
            Icon(
                painter = painterResource(id = R.drawable.icon_arrow),
                contentDescription = stringResource(R.string.arrow),
                tint = NovixTheme.colors.primary
            )
        }
    }
}

@ThemePreviews
@Composable
fun SectionHeaderPreview() {
    NovixTheme {
        SectionHeader(
            text = "New Arrival"
        )
    }
}