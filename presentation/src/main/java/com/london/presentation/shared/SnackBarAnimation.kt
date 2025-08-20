package com.london.presentation.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.component.SnackBar
import kotlinx.coroutines.delay

@Composable
fun SnackBarAnimation(message: String?, icon: Int? = null) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(message) {
        isVisible = true
        delay(3000)
        isVisible = false
    }
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(durationMillis = 300)
        ),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(durationMillis = 300)
        )
    ) {
        SnackBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y = 56.dp),
            title = message ?: stringResource(R.string.incorrect_password),
            icon = painterResource(icon ?: R.drawable.ic_failed)
        )
    }
}
