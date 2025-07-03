package com.london.designsystem.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.designsystem.theme.noRippleClickable

@Composable
fun SaveIcon(
    isSaved: Boolean,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = NovixTheme.colors.iconBackground
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (isSaved) 1f else 0f,
        animationSpec = spring(
            dampingRatio = 0.15f,
            stiffness = 100f
        )
    )
    Box (
        modifier = modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = backgroundColor)
            .border(
                width = 1.dp,
                color = NovixTheme.colors.stroke,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(6.dp)
            .noRippleClickable{
                onSaveClick()
            }
    ) {
        Icon(
            painter = painterResource(R.drawable.icon_remove),
            contentDescription = "Not Save",
            tint = NovixTheme.colors.strokeHigh,
            modifier = Modifier
                .scale(1f - animatedProgress)
                .alpha(1f - animatedProgress)
        )

        Icon(
            painter = painterResource(R.drawable.icon_save),
            contentDescription = "Save",
            tint = NovixTheme.colors.strokeHigh,
            modifier = Modifier
                .scale(animatedProgress)
                .alpha(animatedProgress)
        )
    }
}

@ThemePreviews
@Composable
fun SaveIconPreview() {
    NovixTheme {
        var isSaved by remember { mutableStateOf(true) }
        SaveIcon(
            isSaved = isSaved,
            onSaveClick = { isSaved = !isSaved }
        )
    }
}