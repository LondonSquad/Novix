// designsystem/component/Divider.kt
package com.london.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.london.designsystem.theme.NovixTheme

@Composable
fun Divider(
    modifier: Modifier = Modifier,
    color: Color = NovixTheme.colors.stroke,
    thickness: Dp = 1.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
            .background(color)
    )
}

@Composable
fun SpacerDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 16.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
    )
}