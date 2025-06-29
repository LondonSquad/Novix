package com.london.novix.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            style = NovixTheme.typography.headline.small,
            color = NovixTheme.colors.title,
        )
        Row(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .noRippleClickable(onClick),
        ) {
            Text(
                text = stringResource(R.string.all),
                style = NovixTheme.typography.label.medium,
                color = NovixTheme.colors.primary,
                modifier = Modifier.padding(end = 4.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.icon_right_arrow),
                contentDescription = "right arrow",
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
            text = stringResource(R.string.new_arrival)
        )
    }
}