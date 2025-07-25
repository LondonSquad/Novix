package com.london.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.component.Icon
import com.london.designsystem.component.Text
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.utils.toLocalizedNumbers

@Composable
fun ReviewsDate(
    date: String,
    modifier: Modifier = Modifier,
) {

    if (date.isEmpty() || date.isBlank()) return
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.icon_calender),
            contentDescription = "Calender icon",
            tint = NovixTheme.colors.body,
            modifier = Modifier.size(12.dp)
        )

        Text(
            text = date.toLocalizedNumbers(),
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.body
        )
    }
}