package com.london.presentation.feature.account.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.Icon
import com.london.designsystem.component.Text
import com.london.designsystem.theme.NovixTheme

@Composable
fun AccountMenuItem(
    icon: Painter,
    title: String,
    modifier: Modifier = Modifier,
    endText: String? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AccountMenuItemIcon(
                icon = icon,
                contentDescription = title
            )

            Text(
                text = title,
                style = NovixTheme.typography.label.large,
                color = NovixTheme.colors.title,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        endText?.let {
            Text(
                text = it,
                style = NovixTheme.typography.label.small,
                color = NovixTheme.colors.body
            )
        }
    }
}

@Composable
private fun AccountMenuItemIcon(
    icon: Painter,
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(NovixTheme.colors.surface)
            .border(
                width = 1.dp,
                color = NovixTheme.colors.stroke,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
            tint = NovixTheme.colors.primary,
            modifier = Modifier.size(24.dp)
        )
    }
}