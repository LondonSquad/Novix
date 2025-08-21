package com.london.presentation.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.Icon
import com.london.designsystem.component.Text
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.utils.isNotZeroRate

@Composable
fun TextWithIcon(
    text: String,
    icon: Painter,
    tint: Color = NovixTheme.colors.body,
    hasInitialDot: Boolean = true
) {
    if (text.isEmpty()) return
    val shouldShowDot = hasInitialDot && text.isNotZeroRate()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val scale = LocalDensity.current.fontScale
        val baseIconSize = 12.dp

        if (shouldShowDot) {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(3.dp)
                    .clip(CircleShape)
                    .background(NovixTheme.colors.body)
                    .align(alignment = Alignment.CenterVertically)
            )

            Icon(
                painter = icon,
                contentDescription = stringResource(R.string.image_dot),
                tint = tint,
                modifier = Modifier.size(baseIconSize * scale)
            )
        }
        Text(
            text = text,
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.body,
        )
    }
}
