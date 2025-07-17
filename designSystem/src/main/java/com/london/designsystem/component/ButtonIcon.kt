package com.london.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.noRippleClickable

@Composable
fun ButtonIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconRes: Int,
    backgroundColor: Color = NovixTheme.colors.iconBackground
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = backgroundColor)
            .border(
                width = 1.dp, color = NovixTheme.colors.stroke, shape = RoundedCornerShape(8.dp)
            )
            .padding(6.dp)
            .noRippleClickable (
                onClick
            ), contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = "icon",
            tint = NovixTheme.colors.onPrimary,
            modifier = Modifier
        )
    }
}

