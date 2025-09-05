package com.london.designsystem.snackbar

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically

data class SnackBarAnimationConfig(
    val durationMillis: Int = 300,
    val enterAnimation: EnterTransition =
        slideInVertically(initialOffsetY = { -it }, animationSpec = tween(durationMillis)),
    val exitAnimation: ExitTransition =
        slideOutVertically(
            targetOffsetY = { -2 * it },
            animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
        )
)
