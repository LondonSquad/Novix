package com.london.presentation.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.Icon
import com.london.designsystem.component.Text
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R

@Composable
fun RatingItem(
    modifier: Modifier = Modifier,
    rating: String,
    color: Color = NovixTheme.colors.title
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.star),
            contentDescription = stringResource(R.string.star),
            tint = NovixTheme.colors.yellowAccent
        )
        Text(
            text = rating,
            style = NovixTheme.typography.label.small,
            color = color
        )
    }
}