package com.london.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.RatingBar
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R

@Composable
fun RatingItem(
    modifier: Modifier = Modifier,
    rating: String
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
            color = NovixTheme.colors.title
        )
    }
}