package com.london.novix.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.london.novix.R
import com.london.novix.presentation.designSystem.color.IconBackgroundLight
import com.london.novix.presentation.designSystem.color.StrokeDark
import com.london.novix.presentation.designSystem.color.StrokeHighLight
import com.london.novix.presentation.designSystem.theme.NovixTheme
import com.london.novix.presentation.designSystem.theme.ThemePreviews

@Composable
fun SaveIcon(
    isSaved: Boolean,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (isSaved) 1f else 0f,
        animationSpec = spring(
            dampingRatio = 0.15f,
            stiffness = 100f
        )
    )

    IconButton(
        onClick = onSaveClick,
        modifier = modifier
            .size(32.dp)
            .background(IconBackgroundLight)
            .border(
                width = 1.dp,
                color = StrokeDark,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_remove),
                contentDescription = "Not Save",
                tint = StrokeHighLight,
                modifier = Modifier
                    .scale(1f - animatedProgress)
                    .alpha(1f - animatedProgress)
            )

            Icon(
                painter = painterResource(R.drawable.icon_save),
                contentDescription = "Save",
                tint = StrokeHighLight,
                modifier = Modifier
                    .scale(animatedProgress)
                    .alpha(animatedProgress)
            )
        }
    }
}

//@ThemePreviews
@Preview
@Composable
fun SaveIconPreview() {
    NovixTheme {
        var isSaved by remember { mutableStateOf(false) }
        SaveIcon(
            isSaved = isSaved,
            onSaveClick = { isSaved = !isSaved }
        )
    }
}